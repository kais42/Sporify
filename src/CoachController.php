<?php

namespace App\Controller;

use App\Entity\Coach;
use App\Form\CoachType;
use App\Repository\CartRepository;
use App\Repository\CoachRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Knp\Component\Pager\PaginatorInterface;

class CoachController extends Controller
{
    /**
     * @Route("/coach", name="coach")
     */
    public function index(): Response
    {
        return $this->render('coach/index.html.twig', [
            'controller_name' => 'CoachController',
        ]);
    }
    /**
     * @Route("/admin/coach", name="admin_coach")
     */
    public function coachList(Request $request, EntityManagerInterface $manager,CoachRepository $repository ,PaginatorInterface $coach): Response
    {
        $coach=$repository->findAll();
        $coach = new Coach();
        $form = $this->createForm(CoachType::class,$coach);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $file = $form->get("image")->getData();
            $fileName = $this->generateUniqueFileName().'.'.$file->guessExtension();
            $file->move(
                $this->getParameter('$uploads'),
                $fileName

            );
            $coach->setImage($fileName);
            $nut=$this->getDoctrine()->getManager();
            $nut->persist($coach);
            $nut->flush();
            $this->addFlash('success', 'coach Ajouter!');
            return $this->redirectToRoute('admin_coach');
        }
//pagination
        $allcoach = $repository->findAll();

        $coach = $this->get('knp_paginator')->paginate(
        // Doctrine Query, not results
            $allcoach,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // ItemIt sent!s per page
            3
        );

        return $this->render('pages/admin/coach/liste_coach.html.twig',["form"=>$form->createView(), 'coach'=>$coach]);
    }



    /**
     * @Route("admin/update-coach/{id}", name="update_coach")
     */
    public function update(Request $request, EntityManagerInterface $co, $id): Response
    {
        if ($request->isMethod('post')) {
            // your code
            $coach = $this->getDoctrine()->getRepository(Coach::class)->find($id);
            $coach->setprenom($request->request->get('prenom'));
            $coach->setaddr($request->request->get('addr'));
            $coach->setmail($request->request->get('mail'));
            $coach->setnum($request->request->get('num'));
            $file = $request->files->get('image');
            if (!empty($file)) {
                $fileName = $this->generateUniqueFileName() . '.' . $file->guessExtension();
                $file->move(
                    $this->getParameter('$uploads'),
                    $fileName
                );
                $coach->setimage($fileName);
            }

            $co->flush();

            return $this->redirectToRoute('admin_coach');




        }

    }

    /**
     * @Route("/admin/delete-coach/{id}", name="delete_coach")
     */
    public function delete($id, EntityManagerInterface $manager): Response
    {
        $coach = $this->getDoctrine()->getRepository(Coach::class)->find($id);
        $manager->remove($coach);
        $manager->flush();
        return $this->redirectToRoute('admin_coach');
    }

    /**
     * @Route("/front/liste-coach-front", name="liste_coach_front")
     */
    public function listecoachFront(EntityManagerInterface $manager): Response
    {
        $coach = $this->getDoctrine()->getRepository(Coach::class)->findAll();
        return $this->render('pages/coach/liste_coach_front.html.twig', ["coach"=>$coach]);
    }



    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        return md5(uniqid());
    }

    /**
     * @Route("/front/liste-coach-frontttt", name="mailing")
     */
    public function mail(Request $request ,\Swift_Mailer $mailer,EntityManagerInterface $mail)
    {

        $message = (new \Swift_Message('Hello Email'))
            ->setFrom('soltani.taha90@gmail.com')
            ->setTo( 'nsporify@gmail.com')
            ->setBody('je veut un coch prive')
        ;

        $mailer->send($message);



        return $this->redirectToRoute('liste_coach_front');
    }

}
