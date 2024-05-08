package pkgRobots;

import javax.tools.*;
import java.io.*;
/* 
 * Create Robot
 * Creates and compiles Robocode Java file to test 
 * 
 */
import java.util.Random;

public class createRobot {

	public static void create(double[] chromo) {
		createRobotFile(chromo); // create file
		System.out.println("Criou o robo");
		compile(); // now compile it
		System.out.println("aqui compilou");
	}
	
	public static void compile () {
		System.out.println("entrou no compile()");
		System.out.println(System.getProperty("java.home"));
		
		// robots/custom/JoselitoBot.java
        // D:\Documentos\UFCG\IA\robocode-jgap-template\robots\custom
		// String fileToCompile = "JoselitoBot.java"; // which file to compile * rhyming :) *
		
		String fileToCompile= "C:\\robocode\\robots\\pkg\\RobotGA.java";
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    System.out.println("vai rodar o run()?");
	    compiler.run(null, null, null, fileToCompile); // run compile
	    System.out.println("rodou!");
	}
	
	public static void createRobotFile(double[] chromo){
		try {
			File directory = new File("C:\\robocode\\robots\\pkg\\RobotGA.java");
			if (directory.exists()) {
			    System.out.println("Diretorio encontrado");
			}
			FileWriter fstream = new FileWriter("C:\\robocode\\robots\\pkg\\RobotGA.java"); // file name to create
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write("package pkg;\r\n"
					+ "\r\n"
					+ "import robocode.*;\r\n"
					+ "import robocode.util.Utils;\r\n"
					+ "import java.awt.Color;\r\n"
					+ "import java.util.Random;\r\n"
					+ "\r\n"
					+ "public class RobotGA extends AdvancedRobot {\r\n"
					+ "      Random random = new Random();"
					+ "    final double safeDistanceToWall = " + chromo[1] + ";\r\n"
					+ "	double previousEnemyEnergy = 100;\r\n"
					+ "    long timeSinceLastMove = 0;\r\n"
					+ "	final double maxStationaryTime = " + chromo[3] + ";\r\n"
					+ "    public void run() {\r\n"
					+ "        setColors(Color.blue, Color.black, Color.red);\r\n"
					+ "        setAdjustGunForRobotTurn(true);\r\n"
					+ "        setAdjustRadarForGunTurn(true);\r\n"
					+ "\r\n"
					+ "        do {\r\n"
					+ "            if(getTime() - timeSinceLastMove > maxStationaryTime){\r\n"
					+ "				oscillatoryOrbitMovement();\r\n"
					+ "			}\r\n"
					+ "            turnRadarRightRadians(Double.POSITIVE_INFINITY);\r\n"
					+ "        } while (true);\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    private void oscillatoryOrbitMovement() {\r\n"
					+ "        double direction = Math.random() > 0.5 ? 1 : -1;"
					+ "        double angle = " + chromo[5]+ "* direction;\r\n"
					+ "\r\n"
					+ "        double futureX = getX() + Math.sin(Math.toRadians(getHeading() + angle)) * safeDistanceToWall;\r\n"
					+ "        double futureY = getY() + Math.cos(Math.toRadians(getHeading() + angle)) * safeDistanceToWall;\r\n"
					+ "\r\n"
					+ "        if (!isSafePosition(futureX, futureY)) {\r\n"
					+ "            direction *= -1;\r\n"
					+ "            angle = 120 * direction;\r\n"
					+ "        }\r\n"
					+ "\r\n"
					+ "        setAhead("+ chromo[0]+" * direction);\r\n"
					+ "        setTurnRight(angle);\r\n"
					+ "        execute();\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    private boolean isSafePosition(double x, double y) {\r\n"
					+ "        return x > safeDistanceToWall && x < getBattleFieldWidth() - safeDistanceToWall\r\n"
					+ "                && y > safeDistanceToWall && y < getBattleFieldHeight() - safeDistanceToWall;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "   public void onScannedRobot(ScannedRobotEvent e) {\r\n"
					+ "        double changeInEnergy = previousEnemyEnergy - e.getEnergy();\r\n"
					+ "        if (changeInEnergy >= 0.1 && changeInEnergy <= 3.0) {\r\n"
					+ "            oscillatoryOrbitMovement();\r\n"
					+ "        }\r\n"
					+ "        previousEnemyEnergy = e.getEnergy();\r\n"
					+ "        double radarTurn = Utils.normalRelativeAngle(getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians());\r\n"
					+ "        setTurnRadarRightRadians(2.0 * radarTurn);\r\n"
					+ "\r\n"
					+ "        double bulletPower = calculateBulletPower(e.getDistance());\r\n"
					+ "        double bulletSpeed = 20 - 3 * bulletPower;\r\n"
					+ "        double futureTime = e.getDistance() / bulletSpeed;\r\n"
					+ "        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();\r\n"
					+ "        double futureX = getX() + Math.sin(absoluteBearing) * e.getDistance() + \r\n"
					+ "                         Math.sin(e.getHeadingRadians()) * e.getVelocity() * futureTime;\r\n"
					+ "        double futureY = getY() + Math.cos(absoluteBearing) * e.getDistance() + \r\n"
					+ "                         Math.cos(e.getHeadingRadians()) * e.getVelocity() * futureTime;\r\n"
					+ "        double futureBearing = absoluteBearing(getX(), getY(), futureX, futureY);\r\n"
					+ "\r\n"
					+ "        double gunTurnAmt = Utils.normalRelativeAngle(futureBearing - getGunHeadingRadians());\r\n"
					+ "        setTurnGunRightRadians(gunTurnAmt);\r\n"
					+ "        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {\r\n"
					+ "            fire(bulletPower);\r\n"
					+ "        }\r\n"
					+ "    }\r\n"
					+ "	\r\n"
					+ "    private double calculateBulletPower(double enemyDistance) {\r\n"
					+ "        double power;\r\n"
					+ "        if(getEnergy() >=" + chromo[2]+") {\r\n"
					+ "            power = Math.min(3.0, 400 / enemyDistance);\r\n"
					+ "        } else{\r\n"
					+ "            power = Math.min(2.5, 300 / enemyDistance);\r\n"
					+ "        }"
					+ "        return power;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    public double absoluteBearing(double x1, double y1, double x2, double y2) {\r\n"
					+ "        double deltaX = x2 - x1;\r\n"
					+ "        double deltaY = y2 - y1;\r\n"
					+ "        return Math.atan2(deltaX, deltaY);\r\n"
					+ "    }\r\n"
					+ "    \r\n"
					+ "    @Override\r\n"
					+ "    public void onHitByBullet(HitByBulletEvent event) {\r\n"
					+ "        oscillatoryOrbitMovement();\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    @Override\r\n"
					+ "    public void onHitWall(HitWallEvent event) {\r\n"
					+ "      setBack("+chromo[4] +"); \r\n"
					+ "      setTurnRight(90 + (random.nextDouble() * 180));                 "
					+ "}\r\n"
					+ "}");
			/*out.write("package pkgRobots;\r\n"
					+ "\r\n"
					+ "import robocode.*;\r\n"
					+ "import robocode.util.Utils;\r\n"
					+ "import java.awt.Color;\r\n"
					+ "import java.util.Random;\r\n"
					+ "\r\n"
					+ "public class NewRobotTest extends AdvancedRobot {\r\n"
					+ "    Random random = new Random();\r\n"
					+ "    final int safeDistanceToWall = 70;\r\n"
					+ "    final double maxEscapeAngle = Math.toRadians(45);\r\n"
					+ "	double previousEnemyEnergy = 100;\r\n"
					+ "    long timeSinceLastMove = 0;\r\n"
					+ "	final long maxStationaryTime = 20;\r\n"
					+ "    public void run() {\r\n"
					+ "        setColors(Color.blue, Color.black, Color.red);\r\n"
					+ "        setAdjustGunForRobotTurn(true);\r\n"
					+ "        setAdjustRadarForGunTurn(true);\r\n"
					+ "\r\n"
					+ "        do { \n");
					out.append("if(getTime() - timeSinceLastMove > "+chromo[6]+") { \n");
					out.append
				*/
			/*//start code
			out.write("package custom; \n " +
				"import robocode.util.Utils; \n" +	
				"import robocode.*; \n" +
				"import java.awt.Color;\n" +
				"public class JoselitoBot extends Robot {\n");
			
					// build up robot logic in here
					// access chromosomes from array to set as variables
			
					out.append("int foo = (int)" + chromo[1] + ";\n");
					out.append("\n public void run() {" +
						"\n" +
						"\nsetAdjustGunForRobotTurn(true);" +
						"\nsetAdjustRadarForGunTurn(true);" +
						"\n" +
						"\n		setColors(Color.red,Color.blue,Color.green);" +	
						"\n     while(true) {"
						+ " turnRadarLeft(Double.POSITIVE_INFINITY);" 
						+ "}" +
						"\n" +	
						"\n	}");
					
					out.append("\n	public void onScannedRobot(ScannedRobotEvent e) {" +
							"\n" +
							"\n"  +
							"\n		ahead(" + chromo[0] + ");" +
							"\n" +
							"\n" +
							"\n		turnRight("+ chromo[2] +");"  +
							"\n" +
							"\n " +
							"\n		turnGunRight("+ chromo[6] +");"  +
							"\n" +
							"\n " +
							"\n		turnRadarRight("+ chromo[4] +");"  +
							"\n" +
							"\n" +
							"\n		fire("+ chromo[1] +");"
							+ "\n" +
							"\n		turnLeft("+ chromo[3] +");"  +
							"\n" +
							"\n " +
							"\n		turnGunLeft("+ chromo[7] +");"
						+ "\n		fire("+ chromo[1] +");}");
					
					out.append("\npublic void onHitWall(HitWallEvent e) {" +
							"\n    back(" + chromo[0] + ");" +
							"\n		turnLeft("+ chromo[3] +");"  +
							"\n    ahead(" + chromo[7] + ");" +
							"\n}");
					
					out.append("\npublic void onHitByBullet(HitByBulletEvent e) {" +
					"\n    turnRight(" + chromo[2] + ");" +
					"\n    ahead(" + chromo[0] + " * -1);" +
					"\n}");
					
					// end of robot

			out.append("\n}");
			*/
			out.close(); // close output stream
			
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
