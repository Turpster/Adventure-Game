package com._Turpster.AdventureGame.GameBox;

import com._Turpster.AdventureGame.*;
import java.awt.*;

public class GameBoxOption extends GameBox
{
    Rectangle yesR;
    Rectangle noR;
    String Text;
    String noT;
    String yesT;
    BoxPurpose BP;
    
    public GameBoxOption(final GameBoxHandler gameboxhandler, final String Text, final BoxPurpose BP) {
        super(gameboxhandler);
        this.noT = "No";
        this.yesT = "Yes";
        this.Text = Text;
        this.BP = BP;
        this.yesR = new Rectangle(this.Box.x + this.Box.width / 2 - 50 - 55, this.Box.y + 67, 100, 22);
        this.noR = new Rectangle(this.Box.x + this.Box.width / 2 - 50 + 55, this.Box.y + 67, 100, 22);
    }
    
    public Rectangle getYesR() {
        return this.yesR;
    }
    
    public void setYesR(final Rectangle yesR) {
        this.yesR = yesR;
    }
    
    public Rectangle getNoR() {
        return this.noR;
    }
    
    public void setNoR(final Rectangle noR) {
        this.noR = noR;
    }
    
    @Override
    public void subrender(final Graphics g) {
        final Graphics2D g2D = (Graphics2D)g;
        this.setCloseable(false);
        g.setColor(Color.black);
        g.setFont(new Font("DotumChe Pixel", 0, 13));
        g2D.setComposite(Game.makeTransparent(1.0f));
        g.setColor(new Color(5, 5, 5));
        g.drawRoundRect(this.yesR.x, this.yesR.y, this.yesR.width, this.yesR.height, 5, 5);
        g.drawRoundRect(this.noR.x, this.noR.y, this.noR.width, this.noR.height, 5, 5);
        g.setColor(new Color(255, 255, 255));
        g.fillRoundRect(this.yesR.x, this.yesR.y, this.yesR.width, this.yesR.height, 5, 5);
        g.fillRoundRect(this.noR.x, this.noR.y, this.noR.width, this.noR.height, 5, 5);
        g.setColor(Color.black);
        Game.drawCenteredString(g, this.yesT, this.yesR, g.getFont());
        g.setColor(Color.black);
        Game.drawCenteredString(g, this.noT, this.noR, g.getFont());
        g.setColor(Color.black);
        Game.drawCenteredString(g, this.Text, new Rectangle(this.Box.x, this.Box.y, this.Box.width, this.Box.height - 28), g.getFont());
    }
    
    public void yes() {
        this.destroy();
        if (this.BP == BoxPurpose.newGame) {
            this.game.newGame();
        }
    }
    
    public void no() {
        this.destroy();
    }
    
    public void destroy() {
        super.gbh.getGameBoxes().remove(this);
    }
}
