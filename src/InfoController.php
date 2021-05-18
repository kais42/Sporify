<?php

namespace App\Controller;

use App\Entity\Info;
use App\Form\InfoType;
use App\Repository\InfoRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class InfoController extends Controller
{
    /**
     * @Route("/info", name="info")
     */
    public function index(): Response
    {
        return $this->render('info/index.html.twig', [
            'controller_name' => 'InfoController',
        ]);
    }
    /**
     * @Route("/admin/info", name="admin_info")
     */
    public function infoList(Request $request, EntityManagerInterface $manager,InfoRepository $repository): Response
    {
        $info = $this->getDoctrine()->getRepository(Info::class)->findAll();

        $info = new Info();
        $form = $this->createForm(InfoType::class, $info);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $manager->persist($info);
            $manager->flush();
            return $this->redirectToRoute('admin_info');
        }
       // $info=$repository->findAll();
        $allinfo = $repository->findAll();

        $info = $this->get('knp_paginator')->paginate(
        // Doctrine Query, not results
            $allinfo,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // ItemIt sent!s per page
            3
        );


        return $this->render('pages/admin/info/liste_info.html.twig', ["form"=>$form->createView(), "info"=>$info]);


    }

    /**
     * @Route("admin/update-info/{id}", name="update_info")
     */
    public function update(Request $request, EntityManagerInterface $manager, $id): Response
    {
        if ($request->isMethod('post')) {
            // your code
            $info = $this->getDoctrine()->getRepository(Info::class)->find($id);
            $info->setnom($request->request->get('nom'));
            $info->setprenom($request->request->get('prenom'));
            $info->setmail($request->request->get('mail'));
            $info->setage($request->request->get('age'));
            $info->setdate(\DateTime::createFromFormat('Y-m-d', $request->request->get('date')));
            $info->setexperience($request->request->get('experience'));
            $manager->flush();
            return $this->redirectToRoute('admin_info');
        }

    }

    /**
     * @Route("/admin/delete-info/{id}", name="delete_info")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        $info = $this->getDoctrine()->getRepository(Info::class)->find($id);
        $manager->remove($info);
        $manager->flush();
        return $this->redirectToRoute('admin_info');
    }
    /**
     * @Route("/front/liste-info-front", name="liste_info_front")
     */
    public function listeinfoFront(EntityManagerInterface $manager): Response
    {
        $info = $this->getDoctrine()->getRepository(Info::class)->findAll();
        return $this->render('pages/info/liste_info_front.html.twig', ["info"=>$info]);
    }
    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }



}
