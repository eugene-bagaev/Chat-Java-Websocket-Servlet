import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Start Download");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String filePath = ServletConstants.FILE_DISK_PATH;
        String fileName = request.getParameter(ServletConstants.FILENAME_PARAMETER);

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        ServletOutputStream out = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+fileName);

        File f      = new File(filePath + "/" + fileName);
        System.out.println(f);
        long len    = f.length();

        response.addHeader("Content-Length", String.valueOf(len));
        response.setContentType("application/download");

        FileInputStream fileInputStream = new FileInputStream(f);

        int i;

        while((i = fileInputStream.read()) != -1){
            out.write(i);
        }

        fileInputStream.close();
        out.close();
    }
}
