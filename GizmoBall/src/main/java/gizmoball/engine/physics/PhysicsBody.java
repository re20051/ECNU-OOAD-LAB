package gizmoball.engine.physics;

import gizmoball.engine.geometry.Fixture;
import gizmoball.engine.geometry.Transform;
import gizmoball.engine.geometry.Vector2;
import gizmoball.engine.geometry.shape.AbstractShape;
import lombok.Data;

import java.util.List;

/**
 * 代表一个物体，他也可以是一个组件包含多个基础物体
 */
@Data
public class PhysicsBody {

    /** 物体质量 */
    protected Mass mass;

    /** 当前速度 */
    protected final Vector2 linearVelocity;

    /** 线性阻尼 */
    protected double linearDamping;

    /** 当前受力总和 */
    protected final Vector2 force;

    /** 当前受力列表 */
	protected final List<Force> forces;

    /** 物体组成的部件（简单形状） */
    protected final List<AbstractShape> shapes;

    /**
     * 物体旋转/位移
     */
    protected final Transform transform;

}


