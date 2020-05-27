import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class DrawCircle{
    private Color color;
    private int posX, posY, width, height;

    public DrawCircle(int posX, int posY, int width, int heigth, Color color){
        this.color = color;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = heigth;
    }

    public void draw(GraphicsContext g) {
        g.setFill(color);
        g.fillOval(posX,posY,width,height);
        g.setStroke(Color.BLACK);
        g.strokeOval(posX,posY,width,height);
    }

}
