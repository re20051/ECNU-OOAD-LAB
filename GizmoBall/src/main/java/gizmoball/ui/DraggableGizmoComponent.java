package gizmoball.ui;


import gizmoball.engine.geometry.Transform;
import gizmoball.engine.geometry.Vector2;
import gizmoball.engine.geometry.shape.Circle;
import gizmoball.engine.geometry.shape.QuarterCircle;
import gizmoball.engine.geometry.shape.Rectangle;
import gizmoball.engine.geometry.shape.Triangle;
import gizmoball.engine.physics.PhysicsBody;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.function.Function;

@Getter
@Setter
public class DraggableGizmoComponent extends ImageLabelComponent implements Serializable {

    protected static final Function<Vector2, PhysicsBody> circleBodyCreator = (preferredSize) -> {
        Circle circle = new Circle(preferredSize.x / 2.0);
        return new PhysicsBody(circle);
    };

    protected static final Function<Vector2, PhysicsBody> rectangleBodyCreator = (preferredSize) -> {
        Rectangle rectangle = new Rectangle(preferredSize.x / 2.0, preferredSize.y / 2.0);
        return new PhysicsBody(rectangle);
    };

    protected static final Function<Vector2, PhysicsBody> flipperBodyCreator = (preferredSize) -> {
        Rectangle rectangle = new Rectangle(preferredSize.x / 2.0, preferredSize.y / 4.0 / 2.0);
        return new PhysicsBody(rectangle);
    };

    protected static final Function<Vector2, PhysicsBody> triangleBodyCreator = (preferredSize) -> {
        Vector2[] vertices = new Vector2[]{
                new Vector2(-preferredSize.y / 2.0, -preferredSize.y / 2.0),
                new Vector2(-preferredSize.y / 2.0, preferredSize.y / 2.0),
                new Vector2(preferredSize.x / 2.0, -preferredSize.y / 2.0)
        };
        Triangle triangle = new Triangle(vertices);
        return new PhysicsBody(triangle);
    };

    protected static final Function<Vector2, PhysicsBody> curvedPipeBodyCreator = (preferredSize) -> {
        QuarterCircle quarterCircle = new QuarterCircle(preferredSize.x / 2.0);
        return new PhysicsBody(quarterCircle);
    };

    private GizmoType gizmoType;

    public DraggableGizmoComponent(String resource, String labelText, GizmoType gizmoType) {
        super(resource, labelText);
        this.gizmoType = gizmoType;
    }

    @Override
    public VBox createVBox() {
        VBox vBox = super.createVBox();
        this.getImageView().setCursor(Cursor.HAND);
        return vBox;
    }

    /**
     * Create a physics body for this gizmo.
     * @param preferredSize the preferred size of the gizmo.
     * @param center the center of the gizmo.
     * @return the physics body.
     */
    public PhysicsBody createPhysicsBody(Vector2 preferredSize, Vector2 center) {
        PhysicsBody physicsBody = gizmoType.getPhysicsBodySupplier().apply(preferredSize);
        physicsBody.getShape().translate(center);
        return physicsBody;
    }
}
