/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import javax.swing.JLabel;

/**
 *
 * @author ayman
 */
public class PlayingCard {
    public static enum ShapeTypes{Spade,Diamon,Clubs,RedHeart};
    public int Value;
    public ShapeTypes Shape;
    public String CardName;
    public String ImageName;
    public JLabel Holder;
    
}
