<?php

namespace App\Controller;

use App\Entity\Nutritionniste;
use App\Form\NutritionnisteType;
use App\Repository\NutritionnisteRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bridge\Twig\Mime\NotificationEmail;
use Knp\Component\Pager\PaginatorInterface;

class NutritionnisteController extends Controller
{
    /**
     * @Route("/nutritionniste", name="nutritionniste")
     */
    public function index(): Response
    {
        return $this->render('nutritionniste/index.html.twig', [
            'controller_name' => 'NutritionnisteController',
        ]);
    }
    /**
     * @Route("/admin/nutritionniste", name="admin_nutritionniste")
     */
    public function nutritionnisteList(Request $request, EntityManagerInterface $manager,NutritionnisteRepository $repository ,PaginatorInterface $nutritionniste): Response
    {
       // $nutritionniste = $this->getDoctrine()->getRepository(Nutritionniste::class)->findAll();
        $nutritionniste=$repository->findAll();
        $nutritionniste = new Nutritionniste();
        $form = $this->createForm(NutritionnisteType::class,$nutritionniste);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $file = $form->get("image")->getData();
            $fileName = $this->generateUniqueFileName().'.'.$file->guessExtension();
            $file->move(
                $this->getParameter('$uploads'),
                $fileName

            );
            $nutritionniste->setImage($fileName);
            $nut=$this->getDoctrine()->getManager();
            $nut->persist($nutritionniste);
            $nut->flush();
            $this->addFlash('success', 'Nutrisionniste Ajouter!');
            return $this->redirectToRoute('admin_nutritionniste');
        }
        //notif

       // return $this->render('nutritionniste/liste_nutritionniste.html.twig', ["form"=>$form->createView(), "nutritionniste"=>$nutritionniste]);
       // $nutritionniste=$repository->findAll();

//pagination
        $allnutritionniste = $repository->findAll();

        $nutritionniste = $this->get('knp_paginator')->paginate(
        // Doctrine Query, not results
            $allnutritionniste,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // ItemIt sent!s per page
            3
        );





        return $this->render('pages/admin/nutritionniste/liste_nutritionniste.html.twig',["form"=>$form->createView(), 'nutritionniste'=>$nutritionniste]);
    }



    /**
     * @Route("admin/update-nutritionniste/{id}", name="update_nutritionniste")
     */
    public function update(Request$request, EntityManagerInterface $nut, $id): Response
    {
        if ($request->isMethod('post')) {
            // your code
            $nutritionniste = $this->getDoctrine()->getRepository(Nutritionniste::class)->find($id);
            $nutritionniste->setno($request->request->get('no'));
            $nutritionniste->setprenom($request->request->get('prenom'));
            $nutritionniste->setaddr($request->request->get('addr'));
            $nutritionniste->setmail($request->request->get('mail'));
            $nutritionniste->setnum($request->request->get('num'));
            $file = $request->files->get('image');
            if (!empty($file)) {
                $fileName = $this->generateUniqueFileName() . '.' . $file->guessExtension();
                $file->move(
                    $this->getParameter('$uploads'),
                    $fileName
                );
                $nutritionniste->setimage($fileName);
            }

            $nut->flush();

            $this->addFlash('success', 'Nutrisionniste Modifier!');
            return $this->redirectToRoute('admin_nutritionniste');




        }

    }

    /**
     * @Route("/admin/delete-nutritionniste/{id}", name="delete_nutritionniste")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        $nutritionniste = $this->getDoctrine()->getRepository(Nutritionniste::class)->find($id);
        $manager->remove($nutritionniste);
        $manager->flush();
        $this->addFlash('warning', 'Nutrisionniste Supprimer!');
        return $this->redirectToRoute('admin_nutritionniste');
    }

    /**
     * @Route("/front/liste-nutritionnistet-front", name="liste_nutritionniste_front")
     */
    public function listenutritionnisteFront(EntityManagerInterface $manager): Response
    {
        $nutritionniste = $this->getDoctrine()->getRepository(Nutritionniste::class)->findAll();
        return $this->render('pages/nutritionniste/liste_nutritionniste_front.html.twig', ["nutritionniste"=>$nutritionniste]);
    }



    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }

    /**
     * @Route("/front/liste-nutritionnistet-frontjjjjjj", name="contact")
     */
    public function mail(Request $request ,\Swift_Mailer $mailer,EntityManagerInterface $mail)
    {
        /*  $form=$this->createForm((ContactType::class));
          $form->handleRequest($request);
          if($form->isSubmitted()&& $form->isValid()){
              $contact=$form->getData();

              $message=(new \Swift_Message('nouveau message'))
                  ->setFrom($contact['mail'])
                  ->setTo( $request->request->get('nom'))
                  ->setBody(
                      $request->request->get('message')
                  );

              $mailer->send($message);
              $this->addFlash('message','le message est bien ete envoyer');
              return $this->redirectToRoute('liste_nutritionniste_front');
          }
         return $this->render('nutritionniste/index.html.twig', [
              'contactform' => $form->createView()
          ]);
  */


        $message = (new \Swift_Message('Hello Email'))
            ->setFrom('soltani.taha90@gmail.com')
            ->setTo( 'nsporify@gmail.com')
            ->setBody('je veut prendre un rendez-vous')
        ;

        $mailer->send($message);



        return $this->redirectToRoute('liste_nutritionniste_front');
    }


}
