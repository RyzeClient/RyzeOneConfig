package cc.polyfrost.oneconfig.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

/**
 * Utility class for I/O operations.
 */
public final class IOUtils {

    /**
     * Taken from legui under MIT License
     * <a href="https://github.com/SpinyOwl/legui/blob/develop/LICENSE">https://github.com/SpinyOwl/legui/blob/develop/LICENSE</a>
     */
    @SuppressWarnings("RedundantCast")
    public static ByteBuffer resourceToByteBuffer(String path) throws IOException {
        byte[] bytes;
        path = path.trim();
        if (path.startsWith("http")) {
            bytes = org.apache.commons.io.IOUtils.toByteArray(new URL(path));
        } else {
            InputStream stream;
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                stream = Files.newInputStream(file.toPath());
            } else {
                stream = IOUtils.class.getResourceAsStream(path);
            }
            if (stream == null) {
                throw new FileNotFoundException(path);
            }
            bytes = org.apache.commons.io.IOUtils.toByteArray(stream);
        }
        ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder())
                .put(bytes);
        ((Buffer) data).flip();
        return data;
    }

    public static ByteBuffer resourceToByteBufferNullable(String path) {
        try {
            return resourceToByteBuffer(path);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Copy the specified String to the System Clipboard.
     * @param s the string to copy
     */
    public static void copyStringToClipboard(String s) {
        StringSelection stringSelection = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    /**
     * Return the String on the system clipboard.
     * @return the string on the system clipboard, or null if there is no string on the clipboard or another error occurred.
     */
    public static String getStringFromClipboard() {
        try {
            return Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Copy the given image to the System Clipboard.
     * @param image the image to copy
     */
    public static void copyImageToClipboard(Image image) {
        ImageSelection imageSelection = new ImageSelection(image);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageSelection, null);
    }

    /**
     * Return the image on the system clipboard.
     * @return the image on the system clipboard, or null if there is no image on the clipboard or another error occurred.
     */
    public static Image getImageFromClipboard() {
        try {
            return (Image) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.imageFlavor);
        } catch (Exception e) {
            return null;
        }
    }





    private static class ImageSelection implements Transferable {
        private final Image image;
        public ImageSelection(Image image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @NotNull
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if(!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }
}