import robots.RT2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main
{
    public static void main(String[] args)  //static method
    {
        JFrame jFrame = new JFrame();
        JPanel panel = new JPanel();

        jFrame.setLayout(new FlowLayout());

        jFrame.setSize(640, 480);
        panel.setSize(640, 480);
        JLabel jLabel = new JLabel();

        panel.add(jLabel);
        jFrame.add(panel);
        panel.setVisible(true);
        jFrame.setVisible(true);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RT2 robot = new RT2();
        robot.connect();
        robot.setRightMotorSpeed(30);
        robot.setLeftMotorSpeed(-30);

        long stTime = System.currentTimeMillis();

        System.out.println("asdfghjh");

        BufferedImage icon = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

        while (System.currentTimeMillis() - stTime < 8000)
        {
            System.out.println(robot.getBytesFromCamera().length);
            if (robot.getBytesFromCamera().length > 0)
                icon = Converter(robot.getBytesFromCamera());
            jLabel.setIcon(new ImageIcon(icon));
            jFrame.repaint();
        }

        jFrame.setVisible(false);
        jFrame.dispose();

        robot.setRightMotorSpeed(0);
        robot.setLeftMotorSpeed(0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.disconnect();
    }

    private static BufferedImage Converter(byte[] array)
    {
        int height = 480;
        int width = 640;

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        int ctr=0;

        for(int i=height - 1; i>=0; i--){
            for(int j=0; j<width; j++){
                Color color = new Color(array[ctr] & 0xff, array[ctr + 1] & 0xff, array[ctr + 2] & 0xff);
                newImage.setRGB(j, i, color.getRGB());
                ctr += 3;
            }

        }
        return newImage;
    }
}
