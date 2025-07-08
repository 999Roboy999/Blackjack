import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class Blackjack {

    private Card[] deck;
    private int playersCards = 2;
    public int points = 20;
    public int cardTotal;
    private Color tableBorder = new Color(126, 28, 0);
    private Color table = new Color(62, 145, 115);
    private Color white = new Color(210, 227, 219);
    private Random r = new Random();
    private int[] rectX = {75, 190, 305, 420, 535, 650, 765};
    private int[] rectY = {75, 325, 75, 325, 75, 325, 75};
    private Card[] displayedCards = new Card[7];


    public Blackjack() {
        String[] suits = {"hearts", "diamonds", "spades", "clubs"};
        String[] names = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        deck = new Card[52];
        int index = 0;

        for (String suit : suits) {
            for (int i = 0; i < names.length; i++) {
                deck[index++] = new Card(names[i], values[i], suit);
            }
        }

        shuffle();
        displayedCards[0] = deck[0];
        displayedCards[1] = deck[1];
        cardTotal = displayedCards[0].getValue() + displayedCards[1].getValue();
    }

    public void drawMe(Graphics g) {
        for (int i = 0; i < displayedCards.length; i++) {
            if (displayedCards[i] != null) {  
            displayedCards[i].drawMe(g, rectX[i], rectY[i], 125, 200);
            }
        }
    }

    public void hit() {
        playersCards++;
        displayedCards[playersCards - 1] = deck[playersCards - 1];
        cardTotal += displayedCards[playersCards - 1].getValue();
    }

    public void stand() {
        calculatePoints();
    }

    public void resetGame() {
        playersCards = 2;
        cardTotal = 0;
        points -= 1; 
        shuffle();
        displayedCards[0] = deck[0];
        displayedCards[1] = deck[1];
        cardTotal = displayedCards[0].getValue() + displayedCards[1].getValue();
    }

    public void shuffle() {
        for (int i = 0; i < deck.length; i++) {
            int j = r.nextInt(0, 52);
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

    public void calculatePoints() {
        switch (cardTotal) {
            case 21:
                points += 5;
                break;
            case 20: 
                points += 3;
                break;
            case 19:
                points += 2;
                break;
            case 18:
                points += 2;
                break;
            case 17: 
                points += 1;
                break;
            case 16:
                points += 1;
                break;
        }
    }

    public void drawCard(Graphics g, int i) {
        displayedCards[i].drawMe(g, rectX[i], rectY[i], 125, 200);
    }

    public Card getDrawnCard() {
        return deck[playersCards - 1];
    }

    public int getCardCount() {
        return playersCards;
    }

    public int getRectX(int i) {
        return rectX[i];
    }

    public int getRectY(int i) {
        return rectY[i];
    }

    public int getTotalValue() {
        return cardTotal;
    }

    public int getPoints() {
        return points;
    }

    public int getWinnings() {
        switch (cardTotal) {
            case 21:
                return 5;
            case 20: 
                return 3;
            case 19:
                return 2;
            case 18:
                return 2;
            case 17: 
                return 1;
            case 16:
                return 1;
            default:
                return 0;
        }
    }
}
