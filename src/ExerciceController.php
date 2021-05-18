<?php

namespace App\Controller;

use App\Entity\Exercice;
use App\Form\ExerciceType;
use App\Repository\ExerciceRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ExerciceController extends Controller
{
    /**
     * @Route("/exercice", name="exercice")
     */
    public function index(): Response
    {
        return $this->render('exercice/index.html.twig', [
            'controller_name' => 'ExerciceController',
        ]);
    }


    /**

     * @Route("admin/exercice", name="admin_exercice")
     */
    public function exerciceList(Request $request, EntityManagerInterface $manager,ExerciceRepository $repository): Response
    {
        $exercice=$repository->findAll();
        $exercice = new Exercice();
        $form = $this->createForm(ExerciceType::class,$exercice);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $file = $form->get("image")->getData();
            $fileName = $this->generateUniqueFileName().'.'.$file->guessExtension();
            $file->move(
                $this->getParameter('$uploads'),
                $fileName
            );
            $exercice->setImage($fileName);
            $nut=$this->getDoctrine()->getManager();
            $nut->persist($exercice);
            $nut->flush();
            $this->addFlash('success', 'Exercice Ajouter!');

            return $this->redirectToRoute('admin_exercice');
        }
       // $exercice=$repository->findAll();
        $allexercice = $repository->findAll();

        $exercice = $this->get('knp_paginator')->paginate(
        // Doctrine Query, not results
            $allexercice,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // ItemIt sent!s per page
            3
        );
        return $this->render('pages/admin/exercice/liste_exercice.html.twig',["form"=>$form->createView(), 'exercice'=>$exercice]);

    }




    /**
     * @Route("admin/update-exercice/{id}", name="update_exercice")
     */
    public function update(Request $request, EntityManagerInterface $nut, $id): Response
    {
        if ($request->isMethod('post')) {
            // your code
            $exercice = $this->getDoctrine()->getRepository(Exercice::class)->find($id);
            $exercice->setnom($request->request->get('nom'));
            $exercice->setduration($request->request->get('duration'));
            $exercice->setlocation($request->request->get('location'));
            $file = $request->files->get('image');
            if (!empty($file)){
                $fileName = $this->generateUniqueFileName().'.'.$file->guessExtension();
                $file->move(
                    $this->getParameter('$uploads'),
                    $fileName
                );
                $exercice->setimage($fileName);
            }

            $nut->flush();
            $this->addFlash('success', 'exercice modifier!');

            return $this->redirectToRoute('admin_exercice');
        }

    }

    /**
     * @Route("/admin/delete-exercice/{id}", name="delete_exercice")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        $exercice = $this->getDoctrine()->getRepository(Exercice::class)->find($id);
        $manager->remove($exercice);
        $manager->flush();
        $this->addFlash('success', 'exercice supprimer!');

        return $this->redirectToRoute('admin_exercice');
    }

    /**
     * @Route("/front/liste-exercice-front", name="liste_exercice_front")
     */
    public function listeexerciceFront(EntityManagerInterface $manager): Response
    {
        $exercice = $this->getDoctrine()->getRepository(Exercice::class)->findAll();
        return $this->render('pages/exercice/liste_exercice_front.html.twig', ["exercice"=>$exercice]);
    }



    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }



}
