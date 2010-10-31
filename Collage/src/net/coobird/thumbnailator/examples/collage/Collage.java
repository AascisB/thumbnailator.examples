package net.coobird.thumbnailator.examples.collage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

/**
 * An command-line application which creates a collage from image files.
 * <p>
 * This is an example application using 
 * <a href="http://code.google.com/p/thumbnailator/">Thumbnailator</a>, a
 * thumbnail generation library with a fluent interface for Java.  
 * 
 * @author coobird
 *
 */
public final class Collage
{
	/**
	 * Runs this application.
	 * 
	 * @param args			For the arguments required by this application,
	 * 						please check the {@link #printUsage()} method.
	 */
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			printUsage();
			return;
		}
		
		int lastArg = 0;
		
		String outputFilename = args[lastArg++];
		int width = Integer.parseInt(args[lastArg++]);
		int height = Integer.parseInt(args[lastArg++]);
		
		// Determine the file names of files to make thumbnails of.
		String[] filenames = new String[args.length - lastArg];
		for (int i = 0; i < filenames.length; i++)
		{
			filenames[i] = args[lastArg + i];
		}
		
		// Draw the collage.
		BufferedImage img = drawCollage(filenames, width, height);
		
		// Save the collage image.
		File outputFile = new File(outputFilename);
		try
		{
			ImageIO.write(img, "png", outputFile);
		}
		catch (IOException e)
		{
			System.out.println(
					"File does not exist. Skipping " + outputFile.getName()
			);
		}
	}
	
	/**
	 * Draws the collage.
	 * 
	 * @param filenames			Filenames of the images to use.
	 * @param width				Width of the resulting collage.
	 * @param height			Height of the resulting collage.
	 * @return					The collage.
	 */
	private static BufferedImage drawCollage(
			String[] filenames, int width, int height
	)
	{
		// Create a white image to draw the collage on.
		BufferedImage img = 
			new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = img.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		Random r = new Random();

		// Shrink the image, then rotate it, and draw it on the collage image.
		for (String filename : filenames)
		{
			File f = new File(filename);
			if (!f.exists())
			{
				System.out.println(
						"File does not exist. Skipping " + f.getName()
				);
				continue;
			}
			
			int thumbnailWidth = r.nextInt(width / 2) + (width / 8);
			int thumbnailHeight = r.nextInt(height / 2) + (width / 8);
			int thumbnailX = r.nextInt(width) - (thumbnailWidth / 2);
			int thumbnailY = r.nextInt(height) - (thumbnailHeight / 2);
			double rotationAngle = r.nextDouble() * 360d;
			
			// Thumbnailator used to read, shrink and rotate the source image.
			BufferedImage image = Thumbnails.of(f)
				.size(thumbnailWidth, thumbnailHeight)
				.rotate(rotationAngle)
				.asBufferedImage();
			
			g.drawImage(image, thumbnailX, thumbnailY, null);
		}
		g.dispose();
		
		return img;
	}
	
	/**
	 * Prints the usage for this application.
	 */
	private static void printUsage()
	{
		String usage = 
			"usage: Collage OUTPUT_FILE WIDTH HEIGHT FILE...\n" +
			"\n" +
			"Creates a collage from FILEs. The collage will be saved as\n" +
			"OUTPUT_FILE with dimensions specified by WIDTH and HEIGHT\n" +
			"(in pixels).\n";
		
		System.out.println(usage);
	}
}