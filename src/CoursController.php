<?php

namespace App\Controller;

use App\Entity\Cours;
use App\Form\CoursType;
use App\Repository\CoursRepository;
use Doctrine\ORM\EntityManagerInterface;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class CoursController extends Controller
{
    /**
     * @Route("/cours", name="cours")
     */
    public function index(): Response
    {
        return $this->render('cours/index.html.twig', [
            'controller_name' => 'CoursController',
        ]);
    }

    /**
     * @param Request $request
     * @param EntityManagerInterface $manager
     * @param CoursRepository $repository
     * @return Response
     * @Route("admin/cours", name="admin_cours")
     */
    public function coursList(Request $request, EntityManagerInterface $manager,CoursRepository $repository,PaginatorInterface $cours): Response
    {
        // $cours = $this->getDoctrine()->getRepository(cours::class)->findAll();
        $cours=$repository->findAll();
        $cours = new Cours();
        $form = $this->createForm(CoursType::class,$cours);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $file = $form->get("image")->getData();
            $fileName = $this->generateUniqueFileName().'.'.$file->guessExtension();
            $file->move(
                $this->getParameter('$uploads'),
                $fileName
            );
            $cours->setImage($fileName);
            $nut=$this->getDoctrine()->getManager();
            $nut->persist($cours);
            $nut->flush();
            return $this->redirectToRoute('admin_cours');
        }
        // return $this->render('cours/liste_cours.html.twig', ["form"=>$form->createView(), "cours"=>$cours]);
       // $cours=$repository->findAll();
        $allcours = $repository->findAll();

        $cours = $this->get('knp_paginator')->paginate(
        // Doctrine Query, not results
            $allcours,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // ItemIt sent!s per page
            3
        );


        return $this->render('pages/admin/cours/liste_cours.html.twig',["form"=>$form->createView(), 'cours'=>$cours]);

    }




    /**
     * @Route("admin/update-cours/{id}", name="update_cours")
     */
    public function update(Request $request, EntityManagerInterface $nut, $id): Response
    {
        if ($request->isMethod('post')) {
            // your code
            $cours = $this->getDoctrine()->getRepository(cours::class)->find($id);
            $cours->setnom($request->request->get('nom'));
            $cours->setcategorie($request->request->get('categorie'));
            $cours->setniveau($request->request->get('niveau'));
            $file = $request->files->get('image');
            if (!empty($file)){
                $fileName = $this->generateUniqueFileName().'.'.$file->guessExtension();
                $file->move(
                    $this->getParameter('$uploads'),
                    $fileName
                );
                $cours->setimage($fileName);
            }

            $nut->flush();
            return $this->redirectToRoute('admin_cours');
        }

    }

    /**
     * @Route("/admin/delete-cours/{id}", name="delete_cours")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        $cours = $this->getDoctrine()->getRepository(cours::class)->find($id);
        $manager->remove($cours);
        $manager->flush();
        return $this->redirectToRoute('admin_cours');
    }

    /**
     * @Route("/front/liste-courst-front", name="liste_cours_front")
     */
    public function listecoursFront(EntityManagerInterface $manager): Response
    {
        $cours = $this->getDoctrine()->getRepository(cours::class)->findAll();
        return $this->render('pages/cours/liste_cours_front.html.twig', ["cours"=>$cours]);
    }


    /**
     * @Route("/front/liste-cours-front", name="mail")
     */
    public function mail(Request $request ,\Swift_Mailer $mailer,EntityManagerInterface $mail)
    {  $message = (new \Swift_Message('Reclamation'))
        ->setFrom('soltani.taha90@gmail.com')
        ->setTo( 'nsporify@gmail.com')
        ->setBody('merci pour les cours ')
    ;

        $mailer->send($message);



        return $this->redirectToRoute('liste_cours_front');
    }



    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }


}
