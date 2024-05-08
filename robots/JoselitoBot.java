package custom; 
 import robocode.util.Utils; 
import robocode.*; 
import java.awt.Color;
public class JoselitoBot extends Robot {
int foo = (int)573.160385582956;

 public void run() {

setAdjustGunForRobotTurn(true);
setAdjustRadarForGunTurn(true);

		setColors(Color.red,Color.blue,Color.green);
     while(true) { turnRadarLeft(Double.POSITIVE_INFINITY);}

	}
	public void onScannedRobot(ScannedRobotEvent e) {


		ahead(630.5057009036751);


		turnRight(353.94789507429545);

 
		turnGunRight(503.4256134071222);

 
		turnRadarRight(418.32256664823865);


		fire(573.160385582956);

		turnLeft(557.1113039830001);

 
		turnGunLeft(483.4574843336777);
		fire(573.160385582956);}
public void onHitWall(HitWallEvent e) {
    back(630.5057009036751);
		turnLeft(557.1113039830001);
    ahead(483.4574843336777);
}
public void onHitByBullet(HitByBulletEvent e) {
    turnRight(353.94789507429545);
    ahead(630.5057009036751 * -1);
}
}