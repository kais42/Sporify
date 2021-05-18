<?php

namespace App\Form;

use App\Entity\Promotion;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;


class PromotionType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('titre')
            ->add('pourcentage', NumberType::class, [
                'attr'     => array(
                    'min'  => 0,
                    'max'  => 99,
                    'step' => 1,
                )
            ])
            ->add('description', TextareaType::class)

            ->add('date_debut', DateType::class, [
                'label'=>'Date debut',
                'widget'=>'single_text',
                'required'=>true,
            ])
            ->add('date_fin', DateType::class, [
                'label'=>'Date fin',
                'widget'=>'single_text',
                'required'=>true,
            ])
            ->add('typeProduit', ChoiceType::class, [
                'choices'  => [
                    'Tshirts' => "Tshirts",
                    'Vestes' => "Vestes",
                    'Equipement' => "Equipement",
                    'Pantalons' => "Pantalons" ,
                    'Accessoires' => "Accesoires"
                ],
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Promotion::class,
        ]);
    }
}
