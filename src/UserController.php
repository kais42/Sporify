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
use App\Entity\User;

class UserController extends AbstractController
{
  protected $session;
  public function __construct(SessionInterface $session)
  {
    $this->session = $session;
  }

  /**
   * @Route("/login", name="login")
   */
  public function index(): Response
  {
    return $this->render('pages/user/login.html.twig', [
    ]);
  }

  /**
   * @Route("/signin", name="signin")
   */
  public function signin(Request $request, ValidatorInterface $validator): Response
  {
    $email = $request->request->get("email");
    $password = $request->request->get("password");
    $input = [
      'email' => $email,
      'password' => $password,
    ];
    $constraints = new Assert\Collection([
      'email' => [new Assert\NotBlank],
      'password' => [new Assert\NotBlank],
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
      return $this->render('pages/user/login.html.twig', [
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $user = $this->getDoctrine()->getRepository(User::class)->findOneBy([
      'email' => $email,
    ]);
    if (is_null($user)||password_verify($password, $user->getPassword())==false) {
      return $this->render('pages/user/login.html.twig',['message'=>"Email or password not valid" ]);
    }
    else {
      $this->session->set('user', $user);
      if ($user->getRole() == "client") {
        return $this->redirectToRoute('product');
      } elseif ($user->getRole() == "admin") {
        return $this->redirectToRoute('admin_product');
      }
    }
    return $this->render('pages/user/login.html.twig');
  }

  /**
   * @Route("/signup", name="signup")
   */
  public function signup(Request $request, ValidatorInterface $validator): Response
  {
    $email = $request->request->get("email");
    $phone = $request->request->get("phone");
      $name = $request->request->get("name");
      $address = $request->request->get("address");
    $password = $request->request->get("password");
    $confirm_password = $request->request->get("confirm_password");
    $input = [
      'email' => $email,
        'name' => $name,
        'address' => $address,
        'phone' => $phone,
        'password' => $password,
      'confirm_password' => $confirm_password,
    ];
    $constraints = new Assert\Collection([
      'email' => [new Assert\NotBlank],
        'name' => [new Assert\NotBlank],
        'address' => [new Assert\NotBlank],
        'phone' => [new Assert\NotBlank],
      'password' => [new Assert\NotBlank],
      'confirm_password' => [new Assert\NotBlank],
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
      if ($password !== '' && $confirm_password !== '' && $password !== $confirm_password) {
        $errorMessages->confirm_password = 'confirm password must be equal.';
      }
      return $this->render('pages/user/login.html.twig', [
        'errors' => $errorMessages,
        'old' => $input
      ]);
    }
    
    $user = new User();
    $email = $request->request->get('email');
    $user->setEmail($email);

      $name = $request->request->get('name');
      $user->setFirstName($name);

      $address = $request->request->get('address');
      $user->setAddress($address);

      $phone = $request->request->get('phone');
      $user->setPhone($phone);
    $password = $request->request->get('password');
    $user->setPassword(password_hash($password, PASSWORD_DEFAULT));
    $olduser=$this->getDoctrine()->getRepository(User::class)->findOneBy(['email' => $email]);
    if(is_null($olduser)) {
      $user->setRole('client');
      $doct = $this->getDoctrine()->getManager();
      $doct->persist($user);
      $doct->flush();
      $this->session->clear();
      $this->session->set('user', $user);
      return $this->redirectToRoute('abons');
    }
    $errorMessages = ['message' => 'This email already exist'];
    return $this->render('pages/user/login.html.twig', [
      'errors' => $errorMessages,
    ]);
  }
    /**
     * @Route("/stats", name="stats")
     */
    public function stats(): Response
    {
        $gold=$this->gold();
        $prem=$this->premimum();
        $silver=$this->silver();
        $all=$this->all();
        return $this->render('pages/admin/user/stats.html.twig', array('s'=> $silver,'p'=> $prem,'g'=> $gold,'all'=> $all));

    }

    /**
     * @Route("/myusers", name="myusers")
     */
    public function users(Request $request): Response
    {
        $em = $this->getDoctrine()->getManager();
        $RAW_QUERY = "SELECT * from user";
        $statement = $em->getConnection()->prepare($RAW_QUERY);
        $statement->execute();
        $forums = $statement->fetchAll();
        return $this->render('pages/admin/user/myusers.html.twig', array('forums' => $forums));
    }

    /**
     * @Route("/abns", name="abns")
     */
    public function abns(Request $request): Response
    {
        $em = $this->getDoctrine()->getManager();
        $RAW_QUERY = "SELECT * from abonnement";
        $statement = $em->getConnection()->prepare($RAW_QUERY);
        $statement->execute();
        $forums = $statement->fetchAll();
        $nbr=$this->affgold(5);
        return $this->render('pages/admin/user/myabs.html.twig', array('forums' => $forums,'gold'=>$nbr));
    }


    /**
     * @Route("/abons", name="abons")
     */
    public function abons(): Response
    {
        $em = $this->getDoctrine()->getManager();
        $RAW_QUERY = "SELECT * from abonnement";
        $statement = $em->getConnection()->prepare($RAW_QUERY);
        $statement->execute();
        $forums = $statement->fetchAll();
        return $this->render('pages/user/abon.html.twig', array('forums' => $forums));
    }

  /**
   * @Route("/logout", name="logout")
   */
  public function logout(): Response
  {
    $this->session->clear();
    return $this->redirectToRoute('login');
  }


    /**
     * @Route("/deleteu/{id}", name="deleteu")
     */

    public function deleteu($id)
    {
        $sql = "delete from user where id=:dom";
        $params[':dom'] = $id;
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute($params);
        return $this->redirectToRoute("myusers");
    }

    /**
     * @Route("/deleteb/{id}", name="deleteb")
     */

    public function deleteb($id)
    {
        $sql = "delete from abonnement where id=:dom";
        $params[':dom'] = $id;
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute($params);
        return $this->redirectToRoute("abons");
    }

    public function gold()
    {
        $em = $this->getDoctrine()->getManager();
        $sql="SELECT COUNT(*) as f FROM user where user.Id_ab='Gold'";
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute();
        $a = $stmt->fetchAll();
        return $a;
    }
    public function all()
    {
        $em = $this->getDoctrine()->getManager();
        $sql="SELECT COUNT(*) as f FROM user";
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute();
        $a = $stmt->fetchAll();
        return $a;
    }
    public function silver()
    {
        $em = $this->getDoctrine()->getManager();
        $sql="SELECT COUNT(*) as f FROM user where user.Id_ab='Silver'";
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute();
        $a = $stmt->fetchAll();
        return $a;
    }
    public function premimum()
    {
        $em = $this->getDoctrine()->getManager();
        $sql="SELECT COUNT(*) as f FROM user where user.Id_ab='Premium'";
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute();
        $a = $stmt->fetchAll();
        return $a;
    }


    public function affgold($k)
    {
        $em = $this->getDoctrine()->getManager();
        $sql = "select COUNT(*) as f from user WHERE user.Id_ab='Gold' ";
        $params[':dom'] = $k;
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute($params);
        $a = $stmt->fetchAll();
        return $a;
    }

    public function typeabb($k)
    {
        $em = $this->getDoctrine()->getManager();
        $sql = "select COUNT(*) as f from user WHERE user.Id_ab=:dom ";
        $params[':dom'] = $k;
        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute($params);
        $a = $stmt->fetchAll();
        return $a;
    }

    /**
     * @Route("/ajout_abon", name="ajout_abon")
     */
    public function ajout_abon(Request $request): Response
    {
        $type=$request->query->get('type');
        $duree=$request->query->get('dur');
        $prix=$request->query->get('prix');
        $sql = "INSERT INTO `abonnement` (`type`, `duree`, `prix`) VALUES (:dom, :dom1, :dom2) ";
        $params[':dom'] = $type;
        $params[':dom1'] = $duree;
        $params[':dom2'] = $prix;

        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute($params);
        return $this->redirectToRoute("abns");
    }
    /**
     * @Route("/abonner/{type}", name="abonner")
     */
    public function abonner (Request $request,$type): Response
    {
        $em = $this->getDoctrine()->getManager();
        $user = $this->session->get('user')->getId();
        $em = $this->getDoctrine()->getManager();
        $sql = "update user set user.id_ab=:dom1 where user.id=:dom  ";
        $params[':dom'] = $user;
        $params[':dom1'] = $type;

        $em = $this->getDoctrine()->getManager();
        $stmt = $em->getConnection()->prepare($sql);
        $stmt->execute($params);
        return $this->render('pages/user/login.html.twig', [
        ]);
    }

}
