<?php

namespace App\Controller;

use App\Entity\Regime;
use App\Form\RegimeType;
use App\Repository\RegimeRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class RegimeController extends Controller
{
    /**
     * @Route("/regime", name="regime")
     */
    public function index(): Response
    {
        return $this->render('regime/index.html.twig', [
            'controller_name' => 'RegimeController',
        ]);
    }

    /**
     * @Route("/admin/regime", name="admin_regime")
     */
    public function regimeList(Request $request, EntityManagerInterface $manager, RegimeRepository $repository): Response
    {
        $regime = $this->getDoctrine()->getRepository(Regime::class)->findAll();
        //$regime=$repository->findAll();
        $regime = new Regime();
        $form = $this->createForm(RegimeType::class, $regime);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get("image")->getData();
            $fileName = $this->generateUniqueFileName() . '.' . $file->guessExtension();
            $file->move(
                $this->getParameter('$uploads'),


            );
            $regime->setImage($fileName);
            $reg = $this->getDoctrine()->getManager();
            $reg->persist($regime);
            $reg->flush();
            return $this->redirectToRoute('admin_regime');
        }
        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get("description")->getData();

            // $file stores the uploaded PDF file

            $file = $regime->getdescription();

            $fileName = md5(uniqid()) . '.' . $file->guessExtension();

            // moves the file to the directory where brochures are stored
            $file->move(
                $this->getParameter('$uploads_directory'),
                $fileName
            );
            $regime->setdescription($fileName);
            $reg = $this->getDoctrine()->getManager();
            $reg->persist($regime);
            $reg->flush();
            $this->addFlash('success', 'Regime Ajouter!');

            return $this->redirectToRoute('admin_regime');

        }




    // return $this->render('regime/liste_regime.html.twig', ["form"=>$form->createView(), "regime"=>$regime]);

        $allregime = $repository->findAll();

        $regime= $this->get('knp_paginator')->paginate(
        // Doctrine Query, not results
            $allregime,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            3
        );
        return $this->render('pages/admin/regime/liste_regime.html.twig',["form"=>$form->createView(), 'regime'=>$regime]);


    }

    /**
     * @Route("admin/update-regime/{id}", name="update_regime")
     */
    public function update(Request $request, EntityManagerInterface $nut, $id): Response
    {
        if ($request->isMethod('post')) {
            // your code
            $regime = $this->getDoctrine()->getRepository(Regime::class)->find($id);
            $regime->settype($request->request->get('type'));
            $file = $request->files->get('description');
            if (!empty($file)) {
                $fileName = $this->generateUniqueFileName() . '.' . $file->guessExtension();
                $file->move(
                    $this->getParameter('$uploads_directory'),
                    $fileName
                );
                $regime->setdescription($fileName);
            }            //$regime->setimage($request->request->get('image'));

            $file = $request->files->get('image');
            if (!empty($file)) {
                $fileName = $this->generateUniqueFileName() . '.' . $file->guessExtension();
                $file->move(
                    $this->getParameter('$uploads'),
                    $fileName
                );
                $regime->setimage($fileName);
            }


            $nut->flush();
            $this->addFlash('success', 'Regime Modifier!');

            return $this->redirectToRoute('admin_regime');
        }

    }

    /**
     * @Route("/admin/delete-regime/{id}", name="delete_regime")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        $regime = $this->getDoctrine()->getRepository(Regime::class)->find($id);
        $manager->remove($regime);
        $manager->flush();
        $this->addFlash('warning', 'Regime Supprimer!');

        return $this->redirectToRoute('admin_regime');
    }



    /**
     * @Route("/front/liste-regime-front", name="liste_regime_front")
     */
    public function listenutritionnisteFront(EntityManagerInterface $manager): Response
    {
        $regime = $this->getDoctrine()->getRepository(Regime::class)->findAll();
        return $this->render('pages/regime/liste_regime_front.html.twig', ["regime"=>$regime]);
    }
    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }
}
