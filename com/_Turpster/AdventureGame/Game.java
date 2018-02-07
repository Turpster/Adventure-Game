package com._Turpster.AdventureGame;

import java.util.logging.*;
import com._Turpster.AdventureGame.GameBox.*;
import java.util.*;
import com._Turpster.AdventureGame.Level.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;

import com._Turpster.AdventureGame.Level.Level;
import com._Turpster.AdventureGame.Runnable.*;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    private Thread thread;
    private boolean running;
    private boolean spashscreen;
    private boolean commandCaptured;
    private Level level;
    private double amountOfTicks;
    private double frames;
    public int WIDTH;
    public int HEIGHT;
    private static String NAME;
    private String Commands;
    private String commandFeedback;
    private Window window;
    public static MenuType mt;
    private Handler handler;
    private Commands commands;
    private Logger logger;
    private GameBoxHandler GBH;
    private GUI gui;
    private Fade fade;
    private Camera camera;
    private SaveHandler sh;
    private GameRenderer menu;
    private MouseInput MI;
    private KeyboardInput KI;
    private ArrayList<Character> loadedCharacters;
    Character character;
    private boolean debug;
    
    static {
        Game.NAME = "Unknown Adventure Game";
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public boolean isCommandCaptured() {
        return this.commandCaptured;
    }
    
    public void setCommandCaptured(final boolean commandCaptured) {
        this.commandCaptured = commandCaptured;
    }
    
    public int getWIDTH() {
        return this.WIDTH;
    }
    
    public void setWIDTH(final int wIDTH) {
        this.WIDTH = wIDTH;
    }
    
    public int getHEIGHT() {
        return this.HEIGHT;
    }
    
    public void setHEIGHT(final int hEIGHT) {
        this.HEIGHT = hEIGHT;
    }
    
    public String getCommands() {
        return this.Commands;
    }
    
    public void setCommands(final String commands) {
        this.Commands = commands;
    }
    
    public static MenuType getMt() {
        return Game.mt;
    }
    
    public static void setMt(final MenuType mt) {
        Game.mt = mt;
    }
    
    public Handler getHandler() {
        return this.handler;
    }
    
    public void setHandler(final Handler handler) {
        this.handler = handler;
    }
    
    public Camera getCamera() {
        return this.camera;
    }
    
    public void setCamera(final Camera camera) {
        this.camera = camera;
    }
    
    public Fade getFade() {
        return this.fade;
    }
    
    public void setFade(final Fade fade) {
        this.fade = fade;
    }
    
    public GUI getGui() {
        return this.gui;
    }
    
    public void setGui(final GUI gui) {
        this.gui = gui;
    }
    
    public GameBoxHandler getGBH() {
        return this.GBH;
    }
    
    public void setGBH(final GameBoxHandler gBH) {
        this.GBH = gBH;
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public SaveHandler getSh() {
        return this.sh;
    }
    
    public void setSh(final SaveHandler sh) {
        this.sh = sh;
    }
    
    public Game() {
        this.level = null;
        this.WIDTH = 800;
        this.HEIGHT = 600;
        this.Commands = "> ";
        this.commandFeedback = "";
        this.loadedCharacters = new ArrayList<Character>();
        this.character = null;
        this.debug = true;
        this.WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        Game.mt = MenuType.StartingMenu;
        this.GBH = new GameBoxHandler(this);
        this.logger = this.getLogger();
        this.sh = new SaveHandler(this);
        this.camera = new Camera(this);
        this.commands = new Commands(this);
        this.menu = new GameRenderer(this);
        this.gui = new GUI(this);
        this.fade = new Fade(this);
        this.spashscreen = true;
        this.window = new Window(this.WIDTH, this.HEIGHT, "Adventure Game", this);
        this.handler = new Handler(this);
        this.spashscreen = false;
        this.MI = new MouseInput(this);
        this.KI = new KeyboardInput(this);
        this.level = new Intro();
        this.addMouseListener(this.MI);
        this.addKeyListener(this.KI);
    }
    
    public GameRenderer getMenu() {
        return this.menu;
    }
    
    public void setMenu(final GameRenderer menu) {
        this.menu = menu;
    }
    
    public boolean isDebug() {
        return this.debug;
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    public Character getCharacter() {
        return this.character;
    }
    
    public void setCharacter(final Character character) {
        this.character = character;
    }
    
    public void render() {
        final BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        final Graphics g = bs.getDrawGraphics();
        final Graphics2D g2D = (Graphics2D)g;
        g.setColor(Color.black);
        g.fillRect(0, 0, this.WIDTH, this.HEIGHT);
        if (this.spashscreen) {
            g.setColor(Color.white);
            final Font font = g.getFont();
            g.setFont(new Font("Consolas", 0, 20));
            g.drawString("Turpster Specifications", this.WIDTH / 2 - 126, this.HEIGHT / 2 - 18);
            g.setFont(font);
        }
        else {
            this.handler.render(g);
        }
        if (this.commandCaptured) {
            final Font font = g.getFont();
            g.setFont(new Font("Consolas", 0, 15));
            g2D.setComposite(makeTransparent(0.8f));
            g.setColor(Color.DARK_GRAY);
            g.fillRect(5, 5, 300, 100);
            g2D.setComposite(makeTransparent(1.0f));
            g.setColor(Color.white);
            g.drawString(this.commandFeedback, 7, 25);
            g.setColor(Color.green);
            g.drawString(this.Commands, 7, 15);
            g.setFont(font);
        }
        if (this.debug) {
            g.setColor(Color.yellow);
            g.setFont(new Font("DotumChe Pixel", 1, 15));
            g.drawString(new StringBuilder().append(this.frames).toString(), this.WIDTH - 53, 17);
        }
        this.fade.render(g);
        g.dispose();
        bs.show();
    }
    
    public String getCommandFeedback() {
        return this.commandFeedback;
    }
    
    public void setCommandFeedback(final String commandFeedback) {
        this.commandFeedback = commandFeedback;
    }
    
    public static AlphaComposite makeTransparent(final float alpha) {
        final int type = 3;
        return AlphaComposite.getInstance(type, alpha);
    }
    
    public void tick() {
        this.handler.tick();
        this.fade.tick();
    }
    
    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        this.amountOfTicks = 60.0;
        final double ns = 1.0E9 / this.amountOfTicks;
        double delta = 0.0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (this.running) {
            final long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1.0) {
                while (delta >= 1.0) {
                    this.amountOfTicks = 20.0;
                    this.tick();
                    this.amountOfTicks = 60.0;
                    --delta;
                }
                if (this.running) {
                    this.render();
                }
                ++frames;
                if (System.currentTimeMillis() - timer > 1000L) {
                    timer += 1000L;
                    this.frames = frames;
                    frames = 0;
                }
            }
        }
    }
    
    public synchronized void start() {
        (this.thread = new Thread(this)).start();
        this.running = true;
    }
    
    public synchronized void stop() {
        System.exit(0);
        try {
            this.thread.join();
            this.running = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) {
        new Game();
    }
    
    public void commandExecute() {
        String command = "";
        this.Commands = this.Commands.substring(2);
        final ArrayList<String> args = new ArrayList<String>();
        int argnum = -1;
        char[] charArray;
        for (int length = (charArray = this.Commands.toCharArray()).length, j = 0; j < length; ++j) {
            final char cha = charArray[j];
            if (cha != ' ') {
                if (argnum == -1) {
                    command = String.valueOf(command) + cha;
                }
                else if (args.size() > argnum) {
                    args.set(argnum, String.valueOf(args.get(argnum)) + cha);
                }
                else {
                    args.add(new StringBuilder().append(cha).toString());
                }
            }
            else {
                ++argnum;
            }
        }
        final String[] arguments = new String[args.size()];
        for (int i = 0; i < args.size(); ++i) {
            arguments[i] = args.get(i);
        }
        if (this.commands.onCommand(command, arguments)) {
            this.setCommandFeedback("Command Successful");
        }
        else {
            this.setCommandFeedback("Unknown Command.");
        }
        this.Commands = "> ";
    }
    
    public static void drawCenteredString(final Graphics g, final String text, final Rectangle rect, final Font font) {
        final FontMetrics metrics = g.getFontMetrics(font);
        final int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        final int y = rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
    
    public void newGame() {
        this.fade.setIncrement(0.05f);
        new TurpRunnable(this, 55) {
            @Override
            public void run() {
                Game.this.fade.setIncrement(-0.1f);
                Game.this.newGameSeq();
            }
        };
    }
    
    public void newGameSeq() {
        Game.mt = MenuType.InGame;
        this.menu.setIntro(1);
    }
}
