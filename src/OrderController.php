<?php
namespace App\Controller;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Validator\ValidatorInterface;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\PropertyAccess\PropertyAccess;
use App\Entity\Product;
use App\Entity\Cart;
use App\Entity\Order;
use App\Entity\LineOrder;
use App\Entity\User;
use App\Entity\Promotion;
use \Datetime;
use \DateInterval;
use Dompdf\Dompdf;
use Dompdf\Options;

class OrderController extends AbstractController
{
  protected $session;
  public function __construct(SessionInterface $session)
  {
    $this->session = $session;
  }

  private function isAuth() {
    if(is_null($this->session->get('user'))){
      return false;
    }
    // $user = $this->getDoctrine()->getRepository(User::class)->find($this->session->get('user')->getId());
    // if ($user->getBan()) {
    //   $this->session->clear();
    //   return false;
    // }
    return true;
  }

  private function isAdmin() {
    if(is_null($this->session->get('user'))||$this->session->get('user')->getRole()!="admin"){
      return false;
    }
    // $user = $this->getDoctrine()->getRepository(User::class)->find($this->session->get('user')->getId());
    // if ($user->getBan()) {
    //   $this->session->clear();
    //   return false;
    // }
    return true;
  }
  /**
   * @Route("/cart/add/{id}", name="cart_add")
   */
  public function cart_add($id): Response
  {
    if (!$this->isAuth())
      return $this->redirectToRoute('login');
    $doct = $this->getDoctrine()->getManager();
    $carts = $doct->getRepository(Cart::class)->findAll();
    $is_create = true;
    foreach ($carts as $cart) {
      if ($cart->getProductId() == $id) {
        $is_create = false;
        $cart->setQuantity($cart->getQuantity() + 1);
      }
    }
    if ($is_create) {
      $cart = new Cart();
      $cart->setProductId($id);
      $cart->setUserId($this->session->get('user')->getId());
      $cart->setQuantity(1);
      $doct->persist($cart);
    }
    $doct->flush();
    return $this->redirectToRoute('product');
  }

  /**
   * @Route("/cart/update/{id}", name="cart_update")
   */
  public function cart_update($id, Request $request): Response
  {
    if (!$this->isAuth())
      return $this->redirectToRoute('login');
    $doct = $this->getDoctrine()->getManager();
    $cart = $doct->getRepository(Cart::class)->find($id);
    $quantity = $request->request->get('quantity');
    $cart->setQuantity($quantity);
    $doct->flush();
    return $this->redirectToRoute('product');
  }

  /**
   * @Route("/cart", name="cart")
   */
  public function cart(): Response
  {
    if (!$this->isAuth())
      return $this->redirectToRoute('login');
    $carts = $this->getDoctrine()->getRepository(Cart::class)->findAll();
    foreach ($carts as $cart) {
      $cart->product = $this->getDoctrine()->getRepository(Product::class)->find($cart->getProductId());
    }
    $promotions = $this->getDoctrine()->getRepository(Promotion::class)->findAll();
    return $this->render('pages/order/cart.html.twig', [
      'carts' => $carts,
      'promotions' => $promotions,
    ]);
  }

  /**
   * @Route("/checkout", name="checkout")
   */
  public function checkout(Request $request): Response
  {
    if (!$this->isAuth())
      return $this->redirectToRoute('login');
    $doct = $this->getDoctrine()->getManager();
    $carts = $doct->getRepository(Cart::class)->findAll();
    $total_price = 0;
    foreach ($carts as $cart) {
      $quantity = $request->request->get($cart->getId());
      $cart->setQuantity($quantity);
      $cart->product = $doct->getRepository(Product::class)->find($cart->getProductId());
      $total_price = $total_price + $cart->product->getPrice() * $cart->getQuantity();
    }
    $doct->flush();
    $promocode = $doct->getRepository(Promotion::class)->find($request->request->get('promocode'));
    $promocode_price = number_format($total_price * $promocode->getPourcentage() /100, 2, '.', ' ');
    $shipping_price = number_format($total_price * 19 /100, 2, '.', ' ');
    $total_price = number_format($total_price + $shipping_price + $promocode_price, 2, '.', ' ');;
    return $this->render('pages/order/checkout.html.twig', [
      'carts' => $carts,
      'shipping_price' => $shipping_price,
      'promocode_price' => $promocode_price,
      'total_price' => $total_price,
      'user' => $this->session->get('user'),
    ]);
  }

  /**
   * @Route("/order/store", name="order_store")
   */
  public function order_store(Request $request, ValidatorInterface $validator): Response
  {
    if (!$this->isAuth())
      return $this->redirectToRoute('login');
    $first_name = $request->request->get("first_name");
    $last_name = $request->request->get("last_name");
    $company = $request->request->get("company");
    $shipping_address = $request->request->get("shipping_address");
    $country = $request->request->get("country");
    $zip_code = $request->request->get("zip_code");
    $input = [
      'first_name' => $first_name,
      'last_name' => $last_name,
      'company' => $company,
      'shipping_address' => $shipping_address,
      'country' => $country,
      'zip_code' => $zip_code,
    ];
    $constraints = new Assert\Collection([
      'first_name' => [new Assert\NotBlank],
      'last_name' => [new Assert\NotBlank],
      'company' => [new Assert\NotBlank],
      'shipping_address' => [new Assert\NotBlank],
      'country' => [new Assert\NotBlank],
      'zip_code' => [new Assert\NotBlank],
    ]);
    $violations = $validator->validate($input, $constraints);
    if (count($violations) > 0) {
      $accessor = PropertyAccess::createPropertyAccessor();
      $errorMessages = [];
      foreach ($violations as $violation) {
        $accessor->setValue($errorMessages,
        $violation->getPropertyPath(),
        $violation->getMessage());
      }
      $carts = $this->getDoctrine()->getRepository(Cart::class)->findAll();
      $total_price = 0;
      foreach ($carts as $cart) {
        $cart->product = $this->getDoctrine()->getRepository(Product::class)->find($cart->getProductId());
        $total_price = $total_price + $cart->product->getPrice() * $cart->getQuantity();
      }
      $shipping_price = number_format($total_price * 19 /100, 2, '.', ' ');
      $total_price = number_format($total_price + $shipping_price, 2, '.', ' ');
      return $this->render('pages/order/checkout.html.twig', [
        'carts' => $carts,
        'shipping_price' => $shipping_price,
        'total_price' => $total_price,
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $doct = $this->getDoctrine()->getManager();
    $carts = $this->getDoctrine()->getRepository(Cart::class)->findAll();
    if (!$this->isValidOrder($carts)) {
      $this->cleanCart();
      return $this->render('pages/order/checkout_fail.html.twig', []);
    }

    $user = $doct->getRepository(User::class)->find($this->session->get('user')->getId());
    $first_name = $request->request->get('first_name');
    $user->setFirstName($first_name);
    $last_name = $request->request->get('last_name');
    $user->setLastName($last_name);
    $shipping_address = $request->request->get('shipping_address');
    $user->setAddress($shipping_address);
    $country = $request->request->get('country');
    $user->setCountry($country);
    $this->session->clear();
    $this->session->set('user', $user);
    // save order
    $order = new Order();
    $company = $request->request->get('company');
    $order->setCompany($company);
    $zip_code = $request->request->get('zip_code');
    $order->setZipCode($zip_code);
    $comment = $request->request->get('comment');
    $order->setComment($comment);
    $date = new DateTime();
    $date->add(new DateInterval('P1D'));
    $order->setDate($date);
    $order->setUserId($user->getId());
    $doct->persist($order);
    $doct->flush();
    foreach ($carts as $cart) {
      $this->attachLineOrder($order->getId(), $cart->getProductId(), $cart->getQuantity());
      // remove cart
      $doct->remove($cart);
    }
    $doct->flush();
    return $this->render('pages/order/checkout_success.html.twig', [
    ]);
  }

  private function isValidOrder($carts) {
    $is_validate = true;
    foreach ($carts as $cart) {
      $product = $this->getDoctrine()->getRepository(Product::class)->find($cart->getProductId());
      if ($product->getQuantity() < $cart->getQuantity()) {
        $is_validate = false;
      }
    }
    return $is_validate;
  }

  private function cleanCart() {
    $doct = $this->getDoctrine()->getManager();
    $carts = $doct->getRepository(Cart::class)->findAll();
    foreach ($carts as $cart) {
      $doct->remove($cart);
    }
    $doct->flush();
  }

  /**
   * @Route("/admin/order", name="admin_order")
   */
  public function admin_index(): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $orders = $this->getDoctrine()->getRepository(Order::class)->findAll();
    foreach ($orders as $order) {
      $order->str_date = $order->getDate()->format('Y-m-d');
      $products = $this->getAllOrderProduct($order->getId());
      $total_price = 0;
      $total_number = 0;
      foreach ($products as $product) {
        $total_price = $total_price + $product->product->getPrice() * $product->getQuantity();
        $total_number = $total_number + $product->getQuantity();
      }
      $order->total_price = $total_price;
      $order->total_number = $total_number;
      $order->user = $this->getDoctrine()->getRepository(User::class)->find($order->getUserId());
    }
    return $this->render('pages/admin/order/index.html.twig', [
      'orders' => $orders,
    ]);
  }

  /**
   * @Route("/admin/order/detail/{id}", name="admin_order_detail")
   */
  public function admin_detail($id): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $order = $this->getDoctrine()->getRepository(Order::class)->find($id);
    $products = $this->getAllOrderProduct($order->getId());
    $total_price = 0;
    foreach ($products as $product) {
      $total_price = $total_price + $product->getQuantity() * $product->product->getPrice();
    }
    $order->user = $this->getDoctrine()->getRepository(User::class)->find($order->getUserId());
    return $this->render('pages/admin/order/detail.html.twig', [
      'products' => $products,
      'order' => $order,
      'total_price' => $total_price,
    ]);
  }

  /**
   * @Route("/admin/order/download/{id}", name="admin_order_download")
   */
  public function admin_download($id): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $order = $this->getDoctrine()->getRepository(Order::class)->find($id);
    $order->user = $this->getDoctrine()->getRepository(User::class)->find($order->getUserId());
    $products = $this->getAllOrderProduct($order->getId());
    $total_price = 0;
    foreach ($products as $product) {
      $total_price = $total_price + $product->getQuantity() * $product->product->getPrice();
    }
    $pdfOptions = new Options();
    $pdfOptions->set('defaultFont', 'Arial');
    
    // Instantiate Dompdf with our options
    $dompdf = new Dompdf($pdfOptions);
    
    // Retrieve the HTML generated in our twig file
    $html = $this->renderView('pages/admin/order/pdf.html.twig', [
      'products' => $products,
      'order' => $order,
      'total_price' => $total_price,
    ]);
    
    // Load HTML to Dompdf
    $dompdf->loadHtml($html);
    
    // (Optional) Setup the paper size and orientation 'portrait' or 'portrait'
    $dompdf->setPaper('A4', 'portrait');

    // Render the HTML as PDF
    $dompdf->render();

    // Output the generated PDF to Browser (force download)
    $dompdf->stream( "order_" . $order->getId() . ".pdf", [
      "Attachment" => true
    ]);
  }

  /**
   * @Route("/admin/order/create", name="admin_order_create")
   */
  public function admin_create(): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $products = $this->getDoctrine()->getRepository(Product::class)->findAll();
    return $this->render('pages/admin/order/create.html.twig', [
      'products' => $products
    ]);
  }

  /**
   * @Route("/admin/order/store", name="admin_order_store")
   */
  public function admin_store(Request $request, ValidatorInterface $validator): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $shipping_address = $request->request->get("shipping_address");
    $quantity = $request->request->get("quantity");
    $input = [
      'shipping_address' => $shipping_address,
      'quantity' => $quantity,
    ];
    $constraints = new Assert\Collection([
      'shipping_address' => [new Assert\NotBlank],
      'quantity' => [new Assert\NotBlank],
    ]);
    $violations = $validator->validate($input, $constraints);
    if (count($violations) > 0) {
      $accessor = PropertyAccess::createPropertyAccessor();
      $errorMessages = [];
      foreach ($violations as $violation) {
        $accessor->setValue($errorMessages,
        $violation->getPropertyPath(),
        $violation->getMessage());
      }
      $products = $this->getDoctrine()->getRepository(Product::class)->findAll();
      return $this->render('pages/admin/order/create.html.twig', [
        'products' => $products,
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $order = new Order();
    $shipping_address = $request->request->get('shipping_address');
    $order->setShippingAddress($shipping_address);
    $quantity = $request->request->get('quantity');
    $order->setQuantity($quantity);
    $product_id = $request->request->get('product_id');
    $order->setProductId($product_id);
    $product = $this->getDoctrine()->getRepository(Product::class)->find($product_id);
    $order->setPrice($product->getPrice() * $quantity);
    $date = new DateTime();
    $date->add(new DateInterval('P1D'));
    $order->setDate($date);
    $order->setUserId(1);
    
    // save
    $doct = $this->getDoctrine()->getManager();
    $doct->persist($order);
    $doct->flush();
    return $this->redirectToRoute('admin_order');
  }

  /**
   * @Route("/admin/order/edit/{id}", name="admin_order_edit")
   */
  public function admin_edit($id): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $order = $this->getDoctrine()->getRepository(Order::class)->find($id);
    $products = $this->getDoctrine()->getRepository(Product::class)->findAll();
    return $this->render('pages/admin/order/edit.html.twig', [
      'order' => $order,
      'products' => $products,
    ]);
  }

  /**
   * @Route("/admin/order/update/{id}", name="admin_order_update")
   */
  public function admin_update($id, Request $request, ValidatorInterface $validator): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $shipping_address = $request->request->get("shipping_address");
    $quantity = $request->request->get("quantity");
    $input = [
      'shipping_address' => $shipping_address,
      'quantity' => $quantity,
    ];
    $constraints = new Assert\Collection([
      'shipping_address' => [new Assert\NotBlank],
      'quantity' => [new Assert\NotBlank],
    ]);
    $violations = $validator->validate($input, $constraints);
    if (count($violations) > 0) {
      $accessor = PropertyAccess::createPropertyAccessor();
      $errorMessages = [];
      foreach ($violations as $violation) {
        $accessor->setValue($errorMessages,
        $violation->getPropertyPath(),
        $violation->getMessage());
      }
      $products = $this->getDoctrine()->getRepository(Product::class)->findAll();
      $order = $this->getDoctrine()->getRepository(Order::class)->find($id);
      return $this->render('pages/admin/order/edit.html.twig', [
        'order' => $order,
        'products' => $products,
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $doct = $this->getDoctrine()->getManager();
    $order = $doct->getRepository(Order::class)->find($id);
    $shipping_address = $request->request->get('shipping_address');
    $order->setShippingAddress($shipping_address);
    $quantity = $request->request->get('quantity');
    $order->setQuantity($quantity);
    $product_id = $request->request->get('product_id');
    $order->setProductId($product_id);
    $product = $this->getDoctrine()->getRepository(Product::class)->find($product_id);
    $order->setPrice($product->getPrice() * $quantity);
    $date = new DateTime();
    $date->add(new DateInterval('P1D'));
    $order->setDate($date);
    $order->setUserId(1);
    
    // update
    $doct->flush();
    return $this->redirectToRoute('admin_order', [
      'id' => $order->getId()
    ]);
  }

  /**
   * @Route("/admin/order/delete/{id}", name="admin_order_delete")
   */
  public function admin_delete($id): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $doct = $this->getDoctrine()->getManager();
    $order = $doct->getRepository(Order::class)->find($id);
    $this->removeOrder($order->getId());
    $doct->remove($order);
    $doct->flush();
    return $this->redirectToRoute('admin_order', [
        'id' => $order->getId()
    ]);
  }

  /**
   * @Route("/admin/order/search", name="admin_order_search")
   */
  public function admin_search(Request $request): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $doct = $this->getDoctrine()->getManager();
    $id = $request->request->get('id');
    $filter = [];
    if ($id != '0' && $id != '') {
      $filter['id'] = $id;
      $orders = $doct->getRepository(Order::class)->findWithOrderId($id);
    }
    else {
      $orders = $doct->getRepository(Order::class)->findAll();
    }
    foreach ($orders as $order) {
      $order->product = $this->getDoctrine()->getRepository(Product::class)->find($order->getProductId());
      $order->str_date = $order->getDate()->format('Y-m-d');
    }
    return $this->render('pages/admin/order/index.html.twig', [
      'orders' => $orders,
      'filter' => $filter
    ]);
  }

  // private function generateRandomString($length = 10) {
  //   $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
  //   $charactersLength = strlen($characters);
  //   $randomString = '';
  //   for ($i = 0; $i < $length; $i++) {
  //       $randomString .= $characters[rand(0, $charactersLength - 1)];
  //   }
  //   return $randomString;
  // }

  // private function sizes() {
  //   return ['l', 'm', 's'];
  // }

  private function attachLineOrder($order_id, $product_id, $quantity) {
    $doct = $this->getDoctrine()->getManager();
    $old = $doct->getRepository(LineOrder::class)->findWithFilter(['order_id' => $order_id, 'product_id' => $product_id]);
    if (count($old) === 0) {
      $line_order = new LineOrder();
      $line_order->setOrderId($order_id);
      $line_order->setProductId($product_id);
      $line_order->setQuantity($quantity);
      // save
      $doct->persist($line_order);
      $doct->flush();
    }
  }

  private function detachLineOrder($order_id, $product_id) {
    $doct = $this->getDoctrine()->getManager();
    $old = $doct->getRepository(LineOrder::class)->findWithFilter(['order_id' => $order_id, 'product_id' => $product_id]);
    if (count($old) !== 0) {
      $doct->remove($old[0]);
      $doct->flush();
    }
  }

  private function removeOrder($order_id) {
    $doct = $this->getDoctrine()->getManager();
    $products = $doct->getRepository(LineOrder::class)->findWithFilter(['order_id' => $order_id]);
    foreach ($products as $product) {
      $doct->remove($product);
    }
    $doct->flush();
  }

  private function getAllOrderProduct($order_id) {
    $doct = $this->getDoctrine()->getManager();
    $products = $doct->getRepository(LineOrder::class)->findWithFilter(['order_id' => $order_id]);
    foreach ($products as $product) {
      $product->product = $doct->getRepository(Product::class)->find($product->getProductId());
    }
    return $products;
  }
}
