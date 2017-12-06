/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


/**
 *
 * @author ayman
 */
public class BasraGame extends JFrame implements MouseMotionListener
{
    int playerTurn = 1;
    int player1Score;
    int player2Score;

    ArrayList<PlayingCard> player1Cards=new ArrayList<>();
    ArrayList<PlayingCard> player2Cards=new ArrayList<>();
    ArrayList<PlayingCard> baseCards =new ArrayList<>();
    String CardsName[]=new String[]{"ACE","TWO","THREE","FOUR","FIVE","SIX","SEVEN","EIGHT","NINE","TEN","JACK","QUEEN","KING"};
    ArrayList <PlayingCard> Cards=new ArrayList<PlayingCard>(52);
    String DirectoryPath="D:\\Downloads\\Game\\Cards";
    JLabel XCor=new JLabel("XCor");
    JLabel YCor=new JLabel("YCor");
    JPanel Player1Pane =new JPanel();
    JPanel player2pane = new JPanel();
    JPanel BasePane = new JPanel();

    public BasraGame()
    {
        setTitle("Basra Card Game");
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        setSize(env.getMaximumWindowBounds().width, env.getMaximumWindowBounds().height);
        setLayout(null);
        XCor.setBounds(10,840,120,30);
        YCor.setBounds(130,840,120,30);
        add(XCor);
        add(YCor);
        fillArray("hearts",PlayingCard.ShapeTypes.RedHeart);
        fillArray("clubs",PlayingCard.ShapeTypes.Clubs);
        fillArray("diamonds",PlayingCard.ShapeTypes.Diamon);
        fillArray("spades",PlayingCard.ShapeTypes.Spade);

        player1Cards=RandomFill();
        player2Cards=RandomFill();
        baseCards =RandomFill();
        SetPlayer1Cards();
        setPlayer2Cards();
        setBaseCrads();
        playerTurn = 1;
        Player1Pane.setBounds(490,10,500,165);
        Player1Pane.setBackground(Color.blue);
        player2pane.setBounds(500,600,500,165);
       player2pane.setBackground(Color.red);
        add(player2pane);
        add(Player1Pane);
        BasePane.setBounds(0,300,1550,165);
        BasePane.setBackground(Color.green);
        add(BasePane);
    }

    void refreshView(int finalI,int playerTurn) {
        if(playerTurn==2)
        {
            removePlayer1Cards(finalI);
            SetPlayer1Cards();
        }
        else {
            removeplayer2Cards(finalI);
            setPlayer2Cards();
            if(player2Cards.size()==0)
            {
                player2Cards=RandomFill();
                player1Cards=RandomFill();
                setPlayer2Cards();
                SetPlayer1Cards();
                player2pane.invalidate();
                Player1Pane.invalidate();
                Player1Pane.repaint();
                player2pane.repaint();
            }
        }


        setBaseCrads();
        this.invalidate();
        this.repaint();
        this.doLayout();
    }
    private void removeplayer2Cards(int FinalI)
    {
        player2pane.removeAll();
        player2pane.revalidate();
        player2pane.repaint();
        player2Cards.remove(FinalI);
    }
    private void removeBase()
    {
    BasePane.removeAll();
    BasePane.revalidate();
    BasePane.repaint();
}
    private void removePlayer1Cards(int finalI)

    {
        Player1Pane.removeAll();
        Player1Pane.revalidate();
        Player1Pane.repaint();
        player1Cards.remove(finalI);

    }

    void fillArray(String name, PlayingCard.ShapeTypes Type)
    {
        for (int i=0;i<13;i++)
        {
            PlayingCard p=new PlayingCard();
            p.Value=i;
            p.CardName=CardsName[i];
            p.Shape=Type;
            p.ImageName=DirectoryPath+"\\"+(i+1)+"_of_"+name+".png";
            Cards.add(p);
        }
    }

    void SetPlayer1Cards() {
        int x = 500;
        int y = 10;
        int elementsinrow = 0;
        for (int i = 0; i < player1Cards.size(); i++)
        {
            JLabel j = new JLabel();
            j.setIcon(new ImageIcon(new ImageIcon(player1Cards.get(i).ImageName).getImage().getScaledInstance(120, 160, Image.SCALE_DEFAULT)));
            Player1Pane.add(j);
            int finalI = i;
            j.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(playerTurn == 1) {
                        super.mouseClicked(e);
                        Play(player1Cards.get(finalI));
                        playerTurn=2;
                        refreshView(finalI,playerTurn);
                    }
                }
            });
            j.setBounds(x, y, 120, 160);
            player1Cards.get(i).Holder = j;
            x += 120;
            elementsinrow++;
            if (elementsinrow > 3) {
                x = 0;
                y += 160;
                elementsinrow = 0;
            }
        }
        Player1Pane.invalidate();
        Player1Pane.repaint();
}

    private void addToPlayer(int count)
    {
        if(playerTurn == 1)
            player1Score+=count;
        else
            player2Score+=count;
    }

    private void Play(PlayingCard playingCard) {
        if(playingCard.Value < 10) {
            if(playingCard.Value == 6 && playingCard.Shape == PlayingCard.ShapeTypes.Diamon) {
                //getAll
                int count= baseCards.size();
                baseCards.clear();
                removeBase();
                addToPlayer(count);
            }
            else {
                CheckCalculations(playingCard);
                //check calculations if no match add to base
            }
        }
        else if( playingCard.Value == 10) {
           int count= baseCards.size();
           for (int i = 0; i< baseCards.size(); i++)
            baseCards.get(i).Holder.getParent().remove(baseCards.get(i).Holder);
           baseCards.clear();
           addToPlayer(count);
        }
        else if (playingCard.Value > 10)
        {
            //get Queens or Kings if no match add to base
            int count =CheckCards(playingCard);
            if(count>0)
            {
                addToPlayer(count);
            }
            else
                baseCards.add(playingCard);
                removeBase();
        }
    }

    void CheckCalculations(PlayingCard card)
    {
        boolean found = false;
        int count =CheckCards(card);
        if(count>0)
        {
            found= true;
        }
        addToPlayer(count);
        int sum = 0;
        baseCards.sort((o1, o2) -> (o1.Value - o2.Value) );
        for (int i =1 ;i<baseCards.size();i++)
        {
            List<ArrayList<PlayingCard>> sets = getCombinations(baseCards,i,card.Value+1);

            for(int x = 0 ; x< sets.size(); x++) {
                ArrayList<PlayingCard> subset =  sets.get(x);
                boolean subsetFound = true;
                for(int y = 0; y < subset.size(); y++)
                {
                    if(!baseCards.contains(subset.get(y)))
                        subsetFound = false;
                }

                if(subsetFound) {
                    found=true;
                    for(int y = 0; y < subset.size(); y++)
                    {
                        baseCards.remove(subset.get(y));

                    }
                }
            }
//            if(baseCards.get(i).Value>card.Value)
//            {
//                continue;
//            }
//            for(int j=i+1;j<baseCards.size();j++)
//            {
//                sum = baseCards.get(i).Value+baseCards.get(j).Value;
//                if(sum==card.Value)
//                {
//                    addToPlayer(2);
//                    found =true;
//
//                }
//            }
        }
        removeBase();
        if(!found)
        {
            baseCards.add(card);
        }
    }

    int CheckCards(PlayingCard card)
    {
        int count=0;
        for(int i = 0; i< baseCards.size(); i++)
        {
            if(card.Value== baseCards.get(i).Value)
            {
                count++;
                baseCards.remove(i);
                baseCards.get(i).Holder.getParent().remove(baseCards.get(i).Holder);
            }
        }

        return count;
    }

    void setPlayer2Cards()
    {
       int x=500;
       int y=600;
       int elementsinrow=0;
       for (int i = 0; i < player2Cards.size(); i++)

       {
           JLabel j = new JLabel();
           j.setIcon(new ImageIcon(new ImageIcon(player2Cards.get(i).ImageName).getImage().getScaledInstance(120, 160, Image.SCALE_DEFAULT)));
          player2pane.add(j);
           int finalI = i;
           j.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                   if(playerTurn == 2) {
                       super.mouseClicked(e);
                       Play(player2Cards.get(finalI));
                        playerTurn=1;
                       refreshView(finalI,playerTurn);

                   }
               }
           });
           j.setBounds(x, y, 120, 160);
           player2Cards.get(i).Holder = j;
           x += 120;
          elementsinrow++;
           if (elementsinrow > 3) {
               x = 0;
               y += 160;
               elementsinrow = 0;
           }
       }
        player2pane.invalidate();
       player2pane.repaint();
   }

    void setBaseCrads()
    {
        int x=500;
        if(baseCards.size() > 5) x=400;
        int y=300;
        int elementsinrow=0;
        for (int i = 0; i < baseCards.size(); i++)

        {
            JLabel j = new JLabel();
            j.setIcon(new ImageIcon(new ImageIcon(baseCards.get(i).ImageName).getImage().getScaledInstance(120, 160, Image.SCALE_DEFAULT)));
            BasePane.add(j);
            j.addMouseMotionListener(this);
            j.setBounds(x, y, 120, 160);
            baseCards.get(i).Holder = j;
            x += 120;
            elementsinrow++;
            if (elementsinrow > 6) {
                x = 0;
                y += 160;
                elementsinrow = 0;
            }
        }
    }

    ArrayList<PlayingCard> RandomFill()
    {
        if(Cards.size()==0)
        {
            if(player1Score>player2Score)
            JOptionPane.showMessageDialog(null, "Game Finished,the winner is player 1 with score "+ player1Score, "Winner", JOptionPane.WARNING_MESSAGE);
            else if(player1Score==player2Score)
            {
                JOptionPane.showMessageDialog(null, "Game Finished, DRAW", "winner", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Game Finished, the winner is player 2 with score "+ player2Score, "Winner", JOptionPane.WARNING_MESSAGE);
            }
        }

        ArrayList<PlayingCard> PlayerCards = new ArrayList<>(4);
        for (int i= 0; i< 4 ; i++) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(Cards.size());
            PlayerCards.add(Cards.get(index));
            Cards.remove(index);

        }
    return PlayerCards;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        XCor.setText(""+e.getX());
        YCor.setText(""+e.getXOnScreen());
        for (PlayingCard p: player1Cards)
        {
            if (p.Holder.equals(e.getSource()))
            {
                p.Holder.setBounds(e.getXOnScreen()-10,e.getYOnScreen()-20, 240, 320);

            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }


    private List<ArrayList<PlayingCard>> getCombinations (ArrayList<PlayingCard> input, int k, int cardValue)
    {
        List<ArrayList<PlayingCard>> subsets = new ArrayList<>();

        int[] s = new int[k];                  // here we'll keep indices
        // pointing to elements in input array

        if (k <= input.size()) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++);

            ArrayList<PlayingCard> ss = getSubset(input, s);
            int sum = 0;
            for(PlayingCard playingCard : ss)
                sum += playingCard.Value+1;

            if(sum == cardValue)
                subsets.add(ss);

            for(;;) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == input.size() - k + i; i--);
                if (i < 0) {
                    break;
                }
                s[i]++;                    // increment this item
                for (++i; i < k; i++) {    // fill up remaining items
                    s[i] = s[i - 1] + 1;
                }

                ss = getSubset(input, s);
                sum = 0;
                for(PlayingCard playingCard : ss)
                    sum += playingCard.Value+1;

                if(sum == cardValue)
                    subsets.add(ss);
            }
        }

        return subsets;
    }

    // generate actual subset by index sequence
    ArrayList<PlayingCard> getSubset(ArrayList<PlayingCard> input, int[] subset) {
        ArrayList<PlayingCard> result = new ArrayList<PlayingCard>();
        for (int i = 0; i < subset.length; i++)
            result.add(input.get(subset[i]));
        return result;
    }

}
