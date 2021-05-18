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
use App\Entity\User;

class ProductController extends AbstractController
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
   * @Route("/product", name="product")
   */
  public function index(): Response
  {
    if (!$this->isAuth())
      return $this->redirectToRoute('login');
    $products = $this->getDoctrine()->getRepository(Product::class)->findAll();
    $colors= $this->colors();
    $sizes = $this->sizes();
    $type_names = $this->types();
    $types = [];
    foreach($type_names as $type_name) {
      $type = [];
      $type['name'] = $type_name;
      $type['product_count'] = count($this->getDoctrine()->getRepository(Product::class)->findWithType($type_name));
      array_push($types, $type);
    }
    return $this->render('pages/product/index.html.twig', [
      'products' => $products,
      'colors' => $colors,
      'types' => $types,
      'sizes' => $sizes,
    ]);
  }

  /**
   * @Route("/product/detail/{id}", name="product_detail")
   */
  public function detail($id): Response
  {
    $product = $this->getDoctrine()->getRepository(Product::class)->find($id);
    $sizes = $this->sizes();
    return $this->render('pages/product/detail.html.twig', [
      'product' => $product,
      'sizes' => $sizes
    ]);
  }

  /**
   * @Route("/product/search", name="product_search")
   */
  public function search(Request $request): Response
  {
    $size = $request->request->get('size');
    $color = $request->request->get('color');
    $min_price = $request->request->get('min_price');
    $max_price = $request->request->get('max_price');
    $filter = [];
    if ($size != '0')
      $filter['size'] = $size;
    if ($color != '0')
      $filter['color'] = $color;
    
    $doct = $this->getDoctrine()->getManager();
    $products = $doct->getRepository(Product::class)->findWithFilter($filter, $min_price, $max_price);
    $colors= $this->colors();
    $sizes = $this->sizes();
    $type_names = $this->types();
    $types = [];
    foreach($type_names as $type_name) {
      $type = [];
      $type['name'] = $type_name;
      $type['product_count'] = count($this->getDoctrine()->getRepository(Product::class)->findWithType($type_name));
      array_push($types, $type);
    }
    return $this->render('pages/product/index.html.twig', [
      'products' => $products,
      'colors' => $colors,
      'types' => $types,
      'sizes' => $sizes,
      'filter' => $filter,
      'max_price' => $max_price,
      'min_price' => $min_price,
    ]);
  }

  /**
   * @Route("/product/filter/{type}", name="product_filter_by_type")
   */
  public function filter_by_type($type): Response
  {
    $products = $this->getDoctrine()->getRepository(Product::class)->findWithType($type);
    $colors= $this->colors();
    $sizes = $this->sizes();
    $type_names = $this->types();
    $types = [];
    foreach($type_names as $type_name) {
      $type = [];
      $type['name'] = $type_name;
      $type['product_count'] = count($this->getDoctrine()->getRepository(Product::class)->findWithType($type_name));
      array_push($types, $type);
    }
    return $this->render('pages/product/index.html.twig', [
      'products' => $products,
      'colors' => $colors,
      'types' => $types,
      'sizes' => $sizes,
    ]);
  }

  /**
   * @Route("/admin/product", name="admin_product")
   */
  public function admin_index(): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $products = $this->getDoctrine()->getRepository(Product::class)->findAll();
    foreach ($products as $product) {
      $product->ocolor = $this->getColor($product->getColor());
    }
    $less_products = $this->getDoctrine()->getRepository(Product::class)->findLess(5);
    $colors = $this->colors();
    $types = $this->types();
    $sizes = $this->sizes();
    return $this->render('pages/admin/product/index.html.twig', [
      'products' => $products,
      'colors' => $colors,
      'types' => $types,
      'sizes' => $sizes,
      'less_products' => $less_products,
    ]);
  }

  /**
   * @Route("/admin/product/create", name="admin_product_create")
   */
  public function admin_create(): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $sizes = $this->sizes();
    $types = $this->types();
    $colors = $this->colors();
    return $this->render('pages/admin/product/create.html.twig', [
      'sizes' => $sizes,
      'types' => $types,
      'colors' => $colors,
    ]);
  }

  /**
   * @Route("/admin/product/store", name="admin_product_store")
   */
  public function admin_store(Request $request, ValidatorInterface $validator): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $name = $request->request->get("name");
    $description = $request->request->get("description");
    $quantity = $request->request->get("quantity");
    $type = $request->request->get("type");
    $supplier = $request->request->get("supplier");
    $price = $request->request->get("price");
    $input = [
      'name' => $name,
      'description' => $description,
      'quantity' => $quantity,
      'type' => $type,
      'supplier' => $supplier,
      'price' => $price,
    ];
    $constraints = new Assert\Collection([
      'name' => [new Assert\NotBlank],
      'description' => [new Assert\NotBlank],
      'quantity' => [new Assert\NotBlank],
      'type' => [new Assert\NotBlank],
      'supplier' => [new Assert\NotBlank],
      'price' => [new Assert\NotBlank, new Assert\Type(['type' => 'numeric'])],
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
      $sizes = $this->sizes();
      $types = $this->types();
      $colors = $this->colors();
      return $this->render('pages/admin/product/create.html.twig', [
        'sizes' => $sizes,
        'types' => $types,
        'colors' => $colors,
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $product = new Product();
    $name = $request->request->get('name');
    $product->setName($name);
    $description = $request->request->get('description');
    $product->setDescription($description);
    $quantity = $request->request->get('quantity');
    $product->setQuantity($quantity);
    $type = $request->request->get('type');
    $product->setType($type);
    if ($type === 'Équipement' || $type === 'Accessoires') {
      $product->setSize('');
      $product->setColor('');
    }
    else {
      $size = $request->request->get('size');
      $product->setSize($size);
      $color = $request->request->get('color');
      $product->setColor($color);
    }
    $supplier = $request->request->get('supplier');
    $product->setSupplier($supplier);
    $price = $request->request->get('price');
    $product->setPrice($price);
    
    $image_file = $request->files->get('image');
    if ($image_file) {
      $originalFilename = pathinfo($image_file->getClientOriginalName(), PATHINFO_FILENAME);
      // this is needed to safely include the file name as part of the URL
      $safeFilename = $this->generateRandomString();
      $newFilename = $safeFilename.'.'.$image_file->guessExtension();

      // Move the file to the directory where brochures are stored
      try {
        $image_file->move(
          'upload/products/',
          $newFilename
        );
      } catch (FileException $e) {
        // ... handle exception if something happens during file upload
      }

      // updates the 'brochureFilename' property to store the PDF file name
      // instead of its contents
      $product->setImage('upload/products/'.$newFilename);
    }
    else {
      $errorMessages = ['image' => 'this field is require'];
      $sizes = $this->sizes();
      $types = $this->types();
      $colors = $this->colors();
      return $this->render('pages/admin/product/create.html.twig', [
        'sizes' => $sizes,
        'types' => $types,
        'colors' => $colors,
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    // save
    $doct = $this->getDoctrine()->getManager();
    $doct->persist($product);
    $doct->flush();
    return $this->redirectToRoute('admin_product');
  }

  /**
   * @Route("/admin/product/edit/{id}", name="admin_product_edit")
   */
  public function admin_edit($id): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $product = $this->getDoctrine()->getRepository(Product::class)->find($id);
    $sizes = $this->sizes();
    $types = $this->types();
    $colors = $this->colors();
    return $this->render('pages/admin/product/edit.html.twig', [
      'product' => $product,
      'sizes' => $sizes,
      'types' => $types,
      'colors' => $colors,
    ]);
  }

  /**
   * @Route("/admin/product/update/{id}", name="admin_product_update")
   */
  public function admin_update($id, Request $request, ValidatorInterface $validator): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $name = $request->request->get("name");
    $description = $request->request->get("description");
    $quantity = $request->request->get("quantity");
    $type = $request->request->get("type");
    $supplier = $request->request->get("supplier");
    $price = $request->request->get("price");
    $input = [
      'name' => $name,
      'description' => $description,
      'quantity' => $quantity,
      'type' => $type,
      'supplier' => $supplier,
      'price' => $price,
    ];
    $constraints = new Assert\Collection([
      'name' => [new Assert\NotBlank],
      'description' => [new Assert\NotBlank],
      'quantity' => [new Assert\NotBlank],
      'type' => [new Assert\NotBlank],
      'supplier' => [new Assert\NotBlank],
      'price' => [new Assert\NotBlank],
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
      $sizes = $this->sizes();
      $types = $this->types();
      $colors = $this->colors();
      $product = $this->getDoctrine()->getRepository(Product::class)->find($id);
      return $this->render('pages/admin/product/edit.html.twig', [
        'sizes' => $sizes,
        'types' => $types,
        'colors' => $colors,
        'product' => $product,
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $doct = $this->getDoctrine()->getManager();
    $product = $doct->getRepository(Product::class)->find($id);
    $name = $request->request->get('name');
    $product->setName($name);
    $description = $request->request->get('description');
    $product->setDescription($description);
    $quantity = $request->request->get('quantity');
    $product->setQuantity($quantity);
    $type = $request->request->get('type');
    $product->setType($type);
    if ($type === 'Équipement' || $type === 'Accessoires') {
      $product->setSize('');
      $product->setColor('');
    }
    else {
      $size = $request->request->get('size');
      $product->setSize($size);
      $color = $request->request->get('color');
      $product->setColor($color);
    }
    $supplier = $request->request->get('supplier');
    $product->setSupplier($supplier);
    $price = $request->request->get('price');
    $product->setPrice($price);
    
    $image_file = $request->files->get('image');
    if ($image_file) {
      $originalFilename = pathinfo($image_file->getClientOriginalName(), PATHINFO_FILENAME);
      // this is needed to safely include the file name as part of the URL
      $safeFilename = $this->generateRandomString();
      $newFilename = $safeFilename.'.'.$image_file->guessExtension();

      // Move the file to the directory where brochures are stored
      try {
        $image_file->move(
          'upload/products/',
          $newFilename
        );
      } catch (FileException $e) {
        // ... handle exception if something happens during file upload
      }

      // updates the 'brochureFilename' property to store the PDF file name
      // instead of its contents
      $product->setImage('upload/products/'.$newFilename);
    }
    
    // update
    $doct->flush();
    return $this->redirectToRoute('admin_product', [
      'id' => $product->getId()
    ]);
  }

  /**
   * @Route("/admin/product/delete/{id}", name="admin_product_delete")
   */
  public function admin_delete($id): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $doct = $this->getDoctrine()->getManager();
    $product = $doct->getRepository(Product::class)->find($id);
    $doct->remove($product);
    $doct->flush();
    return $this->redirectToRoute('admin_product', [
        'id' => $product->getId()
    ]);
  }

  /**
   * @Route("/admin/product/search", name="admin_product_search")
   */
  public function admin_search(Request $request): Response
  {
    if (!$this->isAdmin())
      return $this->redirectToRoute('login');
    $size = $request->request->get('size');
    $color = $request->request->get('color');
    $type = $request->request->get('type');
    $filter = [];
    if ($size != '0')
      $filter['size'] = $size;
    if ($color != '0')
      $filter['color'] = $color;
    if ($type != '0')
      $filter['type'] = $type;
    
    $doct = $this->getDoctrine()->getManager();
    $products = $doct->getRepository(Product::class)->findWithFilter($filter, 0, 1000);
    foreach ($products as $product) {
      $product->ocolor = $this->getColor($product->getColor());
    }
    $colors = $this->colors();
    $types = $this->types();
    $sizes = $this->sizes();
    return $this->render('pages/admin/product/index.html.twig', [
      'products' => $products,
      'colors' => $colors,
      'types' => $types,
      'sizes' => $sizes,
      'filter' => $filter
    ]);
  }

  private function generateRandomString($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
  }

  private function sizes() {
    return ['l', 'm', 's'];
  }

  private function types() {
    return [
      'Équipement',
      'Accessoires',
      'Tshirts',
      'Vestes',
      'Pantalons',
    ];
  }

  private function colors() {
    return [
      ['name' => 'red', 'value' => '#ff0000'],
      ['name' => 'blue', 'value' => '#0000ff'],
      ['name' => 'yellow', 'value' => '#00ff00'],
      ['name' => 'green', 'value' => '#008000'],
      ['name' => 'gray', 'value' => '#808080'],
    ];
  }

  private function getColor($name) {
    $colors = $this->colors();
    foreach($colors as $color) {
      if ($color['name'] === $name)
        return $color;
    }
    return ['name' => '', 'value' => ''];
  }
}
