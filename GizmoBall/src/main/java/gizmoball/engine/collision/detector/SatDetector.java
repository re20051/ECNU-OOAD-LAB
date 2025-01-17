package gizmoball.engine.collision.detector;

import gizmoball.engine.collision.Interval;
import gizmoball.engine.collision.Penetration;
import gizmoball.engine.geometry.Vector2;
import gizmoball.engine.geometry.shape.AbstractShape;
import gizmoball.engine.geometry.shape.Circle;

public class SatDetector {

    /**
     * <p>使用于narrowPhase</p>
     * 使用SAT算法判断两个{@link AbstractShape}是否发生碰撞
     *
     * @param shape1      待测图形
     * @param shape2      待测图形
     * @param penetration 穿透信息
     * @return boolean
     */
    public static boolean detect(AbstractShape shape1, AbstractShape shape2, Penetration penetration) {
        // 如果两个都是圆则直接调用CircleDetector
        if (shape1 instanceof Circle && shape2 instanceof Circle) {
            return CircleDetector.detect((Circle) shape1, (Circle) shape2, penetration);
        }
        // 获得焦点数组
        Vector2[] foci1 = shape1.getFoci();
        Vector2[] foci2 = shape2.getFoci();
        // 获得分离轴数组
        Vector2[] axes1 = shape1.getAxes(foci2);
        Vector2[] axes2 = shape2.getAxes(foci1);

        Vector2 currentAxis = null;
        double minOverlap = Double.MAX_VALUE;

        if (axes1 != null) {
            for (Vector2 axis : axes1) {
                if (axis.isZero()) continue;
                // 投影获得分隔
                Interval intervalA = shape1.project(axis);
                Interval intervalB = shape2.project(axis);
                if (!intervalA.overlaps(intervalB)) {
                    return false;
                } else {
                    double overlap = intervalA.getOverlap(intervalB);
                    // 如果分隔存在包含关系
                    if (intervalA.containsExclusive(intervalB) || intervalB.containsExclusive(intervalA)) {
                        double max = Math.abs(intervalA.getMax() - intervalB.getMax());
                        double min = Math.abs(intervalA.getMin() - intervalB.getMin());
                        // 穿透深度为被包含图形的投影长度加上离外层图形的最近投影长度和
                        if (max > min) {
                            // max位于axis的正方向，如果max>min意味着向着负方向移动能更快的分离图形
                            // 所以为了保持分离向量的正方向，需要反转向量
                            axis.negate();
                            overlap += min;
                        } else {
                            overlap += max;
                        }
                    }
                    if (overlap < minOverlap) {
                        minOverlap = overlap;
                        currentAxis = axis;
                    }
                }
            }
        }
        // 同上代码
        if (axes2 != null) {
            for (Vector2 axis : axes2) {
                if (axis.isZero()) continue;
                Interval intervalA = shape1.project(axis);
                Interval intervalB = shape2.project(axis);
                if (!intervalA.overlaps(intervalB)) {
                    return false;
                } else {
                    double overlap = intervalA.getOverlap(intervalB);
                    if (intervalA.containsExclusive(intervalB) || intervalB.containsExclusive(intervalA)) {
                        double max = Math.abs(intervalA.getMax() - intervalB.getMax());
                        double min = Math.abs(intervalA.getMin() - intervalB.getMin());
                        if (max > min) {
                            axis.negate();
                            overlap += min;
                        } else {
                            overlap += max;
                        }
                    }
                    if (overlap < minOverlap) {
                        minOverlap = overlap;
                        currentAxis = axis;
                    }
                }
            }
        }

        Vector2 center1 = new Vector2(shape1.getTransform().getX(), shape1.getTransform().getY());
        Vector2 center2 = new Vector2(shape2.getTransform().getX(), shape2.getTransform().getY());
        Vector2 cToc = center1.to(center2);
        if (cToc.dot(currentAxis) < 0) {
            currentAxis.negate();
        }
        penetration.getNormal().x = currentAxis.x;
        penetration.getNormal().y = currentAxis.y;
        penetration.setDepth(minOverlap);
        return true;
    }
    
}
