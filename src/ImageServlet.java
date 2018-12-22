import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String filePath = "C:\\apache-tomcat-7.0.91\\bin\\files\\";
        String fileName = request.getParameter("filename");

        response.setContentType("image/jpeg");
        System.out.println(filePath + "" + fileName);

        ServletOutputStream out = response.getOutputStream();
        FileInputStream fin     = new FileInputStream(filePath + "/" + fileName);

        BufferedInputStream bin     = new BufferedInputStream(fin);
        BufferedOutputStream bout   = new BufferedOutputStream(out);
        int ch                      = 0;

        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }

        bin.close();
        fin.close();
        bout.close();
        out.close();
    }

}
