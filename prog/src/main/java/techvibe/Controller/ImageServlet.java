package techvibe.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/img/*")
public class ImageServlet extends HttpServlet { //File.separator serve per mettere la barra giusta C:/techvibe_images
    private static final String UPLOAD_FOLDER = "C:" + File.separator + "techvibe_images"; //Cartella dove salvo fisicamente le immagini

    //Riceve la richiesta, trova il file su disco, verifica che esiste e imposta il tipo corretto e invia i byte al browser
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String pathInfo = request.getPathInfo();
        //Se l'url è nullo o solo / non serve a nulla
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //Se l'url è /img/foto.jpg allora pathInfo= /foto.jpg
        //fileName="foto.jpg"
        String fileName = pathInfo.substring(1); // Rimuove il "/"
       //Costruisce il percorso completo del file sul disco, cioè tutto il path
        Path imagePath = Paths.get(UPLOAD_FOLDER, fileName);

        //Controlla che il file esista
        if (!Files.exists(imagePath) || !Files.isRegularFile(imagePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //Determina il tipo di file
        String contentType = getServletContext().getMimeType(fileName);
        if (contentType == null) {
            //Se non lo capisce usa il file binario generico
            contentType = "application/octet-stream";
        }

        //Imposta la risposta con il tipo del file e la dimensione
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(imagePath));
        //Copia il contenuto dle file nel flusso di output e invia i byte al browser
        Files.copy(imagePath, response.getOutputStream());
    }
}