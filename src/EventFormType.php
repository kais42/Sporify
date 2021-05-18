<?php

namespace App\Form;

use App\Entity\Event;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Form\Extension\Core\Type\FileType;

class EventFormType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('titre')
            ->add('location')
            ->add('description',  TextareaType::class)
            ->add('dateDebut', DateType::class, [
                'label'=>'Date debut',
                'widget'=>'single_text',
                'required'=>true,
            ])
            ->add('dateFin', DateType::class, [
                'label'=>'Date fin',
                'widget'=>'single_text',
                'required'=>true,
            ])
            ->add('image', FileType::class, ['constraints' => [
                new File([
                    'maxSize' => '9000k',
                    'mimeTypes' => [
                        'image/jpeg',
                        'image/png',
                    ],
                    'mimeTypesMessage' => 'Please upload a valid image',
                ])
            ]])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Event::class,
        ]);
    }
}
