package techvibe.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet("/Upload")
public class UploadServlet extends HttpServlet {

    // USA UNA CARTELLA FUORI DA TOMCAT CHE NON SI CANCELLA MAI
    private static final String UPLOAD_FOLDER = "C:" + File.separator + "techvibe_images";
    // Su Mac/Linux: "/var/techvibe_images" oppure System.getProperty("user.home") + "/techvibe_images"

    @Override
    public void init() throws ServletException {
        super.init();
        File uploadDir = new File(UPLOAD_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("✅ Cartella persistente creata: " + UPLOAD_FOLDER);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lista dei file caricati
        List<Part> fileParts = request.getParts().stream()
                .filter(part -> "file".equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());

        ArrayList<String> uploadedList = new ArrayList<>();

        for(Part filePart : fileParts) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Genera nome unico
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            File destinationFile = new File(UPLOAD_FOLDER, uniqueFileName);

            // Se il file esiste già, aggiungi un numero
            int counter = 1;
            while (destinationFile.exists()) {
                String nameWithoutExt = uniqueFileName.substring(0, uniqueFileName.lastIndexOf('.'));
                String extension = uniqueFileName.substring(uniqueFileName.lastIndexOf('.'));
                uniqueFileName = nameWithoutExt + "_" + counter + extension;
                destinationFile = new File(UPLOAD_FOLDER, uniqueFileName);
                counter++;
            }

            // Salva il file nella cartella persistente
            try (InputStream fileInputStream = filePart.getInputStream()) {
                Files.copy(fileInputStream, destinationFile.toPath());
            }

            uploadedList.add(uniqueFileName);
        }

        request.setAttribute("uploadedList", uploadedList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/views/crm/upload.jsp");
        requestDispatcher.forward(request, response);
    }
}