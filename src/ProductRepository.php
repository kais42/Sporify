<?php

namespace App\Repository;

use App\Entity\Product;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Product|null find($id, $lockMode = null, $lockVersion = null)
 * @method Product|null findOneBy(array $criteria, array $orderBy = null)
 * @method Product[]    findAll()
 * @method Product[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ProductRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Product::class);
    }

    /**
     * @return Product[]
     */
    public function findWithFilter($filter, $min_price, $max_price): array
    {
        $query = $this->createQueryBuilder('product');
        foreach ($filter as $key => $value)
        {
            $query->andWhere($query->expr()->andX(
                $query->expr()->like("product.".$key, ":keyword_".$key)               
            ));
            $query->setParameter("keyword_".$key, '%'.$value.'%');
        }
        $query->andWhere('product.price > '.$min_price);
        $query->andWhere('product.price < '.$max_price);
        return $query->orderBy('product.id', 'ASC')
            ->getQuery()
            ->getResult();
    }

    /**
     * @return Product[]
     */
    public function findWithType($type): array
    {
        $entityManager = $this->getEntityManager();

        $query = $entityManager->createQuery(
            "SELECT product
            FROM App\Entity\Product product
            WHERE product.type = '" . $type . "'
            ORDER BY product.id ASC"
        );

        // returns an array of Product objects
        return $query->getResult();
    }

    /**
     * @return Product[]
     */
    public function findLess($number): array
    {
        $entityManager = $this->getEntityManager();

        $query = $entityManager->createQuery(
            "SELECT product
            FROM App\Entity\Product product
            WHERE product.quantity < '" . $number . "'
            ORDER BY product.id ASC"
        );

        // returns an array of Product objects
        return $query->getResult();
    }

    // /**
    //  * @return Product[] Returns an array of Product objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('p')
            ->andWhere('p.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('p.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?Product
    {
        return $this->createQueryBuilder('p')
            ->andWhere('p.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
