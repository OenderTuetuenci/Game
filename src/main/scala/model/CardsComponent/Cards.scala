package model

import scala.util.Random

case class Cards() extends CardsInterface() {
    override def drawCard(deck: Vector[String]): Vector[String] = {
        super.drawCard(deck)
    }

    override def shuffleChanceCards(cardList: List[String]): Vector[String] = {
        var deck: Vector[String] = Vector[String]()
        for (card <- cardList) deck = deck :+ card
        Random.shuffle(deck)
    }

    override def shuffleCommunityChestCards(cardList: List[String]): Vector[String] = {
        var deck: Vector[String] = Vector[String]()
        for (card <- cardList) deck = deck :+ card
        Random.shuffle(deck)

    }
}