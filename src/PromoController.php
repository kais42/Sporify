<?php

namespace App\Controller;

use App\Entity\Promotion;
use App\Form\PromotionType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

class PromoController extends AbstractController
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
     * @Route("/admin/promos", name="admin_promos")
     */
    public function promoList(Request $request, EntityManagerInterface $manager): Response
    {
        if (!$this->isAdmin())
            return $this->redirectToRoute('login');
        $promos = $this->getDoctrine()->getRepository(Promotion::class)->findAll();

        $promo = new Promotion();
        $form = $this->createForm(PromotionType::class, $promo);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $manager->persist($promo);
            $manager->flush();
            return $this->redirectToRoute('admin_promos');
        }
        return $this->render('pages/admin/promo/liste_promo.html.twig', ["form"=>$form->createView(), "promos"=>$promos]);
    }

    /**
     * @Route("admin/update-promo/{id}", name="update_promo")
     */
    public function update(Request $request, EntityManagerInterface $manager, $id): Response
    {
        if (!$this->isAdmin())
            return $this->redirectToRoute('login');
        if ($request->isMethod('post')) {
            // your code
            $promo = $this->getDoctrine()->getRepository(Promotion::class)->find($id);
            $promo->setTitre($request->request->get('titre'));
            $promo->setTypeProduit($request->request->get('type_produit'));
            $promo->setPourcentage($request->request->get('pourcentage'));
            $promo->setDescription($request->request->get('description'));
            $promo->setDateDebut(\DateTime::createFromFormat('Y-m-d', $request->request->get('DateDebut')));
            $promo->setDateFin(\DateTime::createFromFormat('Y-m-d', $request->request->get('DateFin')));


            $manager->flush();
            return $this->redirectToRoute('admin_promos');
        }

    }

    /**
     * @Route("/admin/delete-promo/{id}", name="delete_promo")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        if (!$this->isAdmin())
            return $this->redirectToRoute('login');
        $promo = $this->getDoctrine()->getRepository(Promotion::class)->find($id);
        $manager->remove($promo);
        $manager->flush();
        return $this->redirectToRoute('admin_promos');
    }
    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }


}

