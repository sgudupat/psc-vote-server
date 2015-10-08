<html>
<head>
<title>Image Upload</title>
</head>
<body>
<%@ page import ="sun.misc.BASE64Decoder"%>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="java.io.ByteArrayInputStream"%>
<%@ page import ="java.io.File"%>
<%@ page import ="java.io.IOException"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="org.apache.commons.lang.StringUtils"%>

 <%
   System.out.println("Start image upload::");
   String imagec= request.getParameter("image");
   String fileName = request.getParameter("filename");
   String extn = StringUtils.substringAfter(fileName, ".");

   System.out.println("imagec::" + imagec);
   System.out.println("fileName::" + fileName);
   System.out.println("extn::" + extn);
 %>
 <% BufferedImage image = null;
        byte[] imageByte;
   System.out.println("Before Try:");
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imagec);
   System.out.println("After Image Decode:");
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
   System.out.println("After Image Built:");
			ImageIO.write(image, extn,new File("/home/ec2-user/tomcat-7.0.63/webapps/images/" + fileName));
   System.out.println("After Image Write:");
            bis.close();
   System.out.println("End:");
        } catch (Exception e) {
			System.out.println("Error::" + e.getMessage());
            e.printStackTrace();
        }%>
</body>
</html>