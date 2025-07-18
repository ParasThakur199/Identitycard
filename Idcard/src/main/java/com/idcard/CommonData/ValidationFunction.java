package com.idcard.CommonData;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;

@Service
public class ValidationFunction {
	public boolean isNullValue(String value)
	{
		if( value==null || value.isBlank()|| value.isEmpty())
		{
			return true;
		}
		return false;
	}
	public String isNullCheckreturnValue(String input) {
		if (input == null || input.isEmpty() || input.isBlank()) {
			return "";
		} else {
			return input.trim();
		}
	} 
	public String removeWhiteSpace(String input)
	{
		 	input = input.trim(); 
	        input = input.replaceAll("\\s+", " ");
	        return input;
	}
	
	
	public String convertSHA256(String password) {
		
		try { 
            MessageDigest digest = MessageDigest.getInstance("SHA-256");  
            byte[] passwordBytes = password.getBytes();  
            byte[] hashBytes = digest.digest(passwordBytes); 
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            } 
            String sha256Hash = hexString.toString(); 
          return sha256Hash;
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
		return null;
	}
	
	
	
	public boolean isPdfFile(MultipartFile file) {
		if (file == null || file.getContentType() == null) {
			return false; // Return false if the file is null or the content type is null
		}

		// First, check the MIME type using the content type from the file
		String contentType = file.getContentType();
		if (!contentType.equalsIgnoreCase("application/pdf")) {
			return false; // Return false if the MIME type is not PDF
		}
		// Check if the filename ends with ".pdf" and contains any suspicious extensions
		String filename = file.getOriginalFilename().toLowerCase();
		if (filename.endsWith(".pdf")) {

			if (filename.matches(".*\\.(exe|bat|sh|cmd|js|php|dll|bin|msi|scr|vbs)\\.pdf$")) {
				// If the filename contains ".exe", ".bat", or ".msi", reject the file
				return false;
			}
		} else {
			return false; // Return false if the file does not have a ".pdf" extension
		}

		// Further validation: Check the actual file content using the file's
		// InputStream
		try (InputStream inputStream = file.getInputStream()) {
			 byte[] buffer = new byte[4];
		        if (inputStream.read(buffer) < 4 || buffer[0] != 0x25 || buffer[1] != 0x50 || buffer[2] != 0x44 || buffer[3] != 0x46) {
		            return false;
		        }

		        // Load PDF using iText
		        try (PdfReader reader = new PdfReader(file.getInputStream());
		             PdfDocument pdfDoc = new PdfDocument(reader)) {

		            // Check for JavaScript Actions (OpenAction, JavaScript annotations)
		            PdfDictionary catalog = pdfDoc.getCatalog().getPdfObject();
		            
		            // 1️⃣ Check OpenAction for JavaScript
		            PdfObject openAction = catalog.get(PdfName.OpenAction);
		            if (openAction instanceof PdfDictionary) {
		                PdfDictionary openActionDict = (PdfDictionary) openAction;
		                if (openActionDict.containsKey(PdfName.JS) || openActionDict.containsKey(PdfName.JavaScript)) {
		                    return false; // JavaScript found in OpenAction
		                }
		            }

		            // 2️⃣ Check for JavaScript in Annotations
		            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
		                List<PdfAnnotation> annotations = pdfDoc.getPage(i).getAnnotations();
		                for (PdfAnnotation annotation : annotations) {
		                    PdfDictionary annotDict = annotation.getPdfObject();
		                    if (annotDict.containsKey(PdfName.JS) || annotDict.containsKey(PdfName.JavaScript)) {
		                        return false; // JavaScript found in annotations
		                    }
		                }
		            }

		            // 3️⃣ Check for JavaScript in Embedded Actions
		            PdfObject names = catalog.get(PdfName.Names);
		            if (names instanceof PdfDictionary) {
		                PdfDictionary namesDict = (PdfDictionary) names;
		                if (namesDict.containsKey(PdfName.JavaScript)) {
		                    return false; // JavaScript found in names dictionary
		                }
		            }
		        }

		        return true; // PDF 
		} catch (IOException e) {
			// Handle the case where we can't read the file content
			System.out.println("error:" + e.getMessage());
			return false;
		}
	}
	    public boolean isFileSizeValid(MultipartFile file, long maxSizeInBytes) { 
	   // long a=	file.getSize();
	        return file != null && file.getSize() <= maxSizeInBytes;
	    }
	    public boolean isImageFile(MultipartFile file) {
	        return file != null && file.getContentType() != null &&
	               (file.getContentType().equalsIgnoreCase("image/jpeg") ||file.getContentType().equalsIgnoreCase("image/jpg") || file.getContentType().equalsIgnoreCase("image/png"));
	    }
	
	public boolean isValidNumber(String value) {
		String regex = "[0-9]*";
		if (value != null) {
			boolean val = Pattern.matches(regex, value.trim());
			if (!val) {
				return true;
			}
		}
		return false;
	}
	public boolean isValid(String value) {
		String regex = "[A-Za-z0-9 ., ]*$";
		if (value != null) {
			boolean val = Pattern.matches(regex, value.trim());
			if (!val) {
				return true;
			}
		}
		return false;
	}
	public boolean isValidName(String value) {
		String regex = "[A-Za-z ]*$";
		if (value != null) {
			boolean val = Pattern.matches(regex, value.trim());
			if (!val) {
				return true;
			}
		}
		return false;
	}
	public boolean isValidMobile(String value) {
		String regex = "[5-9]{1}[0-9]{9}";
		if (value != null) {
			boolean val = Pattern.matches(regex, value.trim());
			if (!val) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidEmail(String email) {
		String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return !matcher.matches();
	}
	public boolean isValidEmailWithDomain(String email,String domainname) {
		String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"+domainname+"$";
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return !matcher.matches();
	}

	public boolean isValidDate(String value) {
		value = value.replaceAll("-", "/");
		String dateFormat1 = "dd/MM/yyyy";
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false); // Disable lenient parsing
		try {
			sdf.parse(value);
			return true;
		} catch (Exception e) {
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
				sdf1.setLenient(false);
				sdf1.parse(value);
				return true;
			} catch (Exception e1) {
				return false;
			}
		}
	}
	
	public boolean isValidLatitude(String latitude) { 
	        String regex = "^([-+]?)([0-9]{1,2}(?:\\.[0-9]+)?|90(?:\\.0+)?)$";
	        return latitude.matches(regex);
	    }

	public boolean isValidLongitude(String longitude) { 
	        String regex = "^([-+]?)([0-9]{1,3}(?:\\.[0-9]+)?|180(?:\\.0+)?)$";
	        return longitude.matches(regex);
	    }
	 public String maskPhoneNumber(String phoneNumber) {
	        if (phoneNumber == null || phoneNumber.length() < 6) {
	            return phoneNumber;
	        } 
	        int length = phoneNumber.length(); 
	        int numToMask = length - 4; 
	        StringBuilder maskedBuilder = new StringBuilder(); 
	        maskedBuilder.append(phoneNumber, 0, 2); 
	        for (int i = 0; i < numToMask; i++) {
	            maskedBuilder.append('*');
	        } 
	        maskedBuilder.append(phoneNumber.substring(length - 4)); 
	        return maskedBuilder.toString();
	    }
	 public boolean validatePassword(String password) {
	        String PASSWORD_PATTERN ="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[#$@]).{6,50}$";
	        return password.matches(PASSWORD_PATTERN);
	    }
	 public String maskEmail(String email) {
		 int startChars=3;
		  int endChars=1;
	        String[] parts = email.split("@");
	        if (parts.length != 2) {
	            throw new IllegalArgumentException("Invalid email address");
	        }

	        String username = parts[0];
	        String domain = parts[1]; 
	        StringBuilder maskedUsername = new StringBuilder();
	        if (username.length() <= startChars + endChars) {
	             maskedUsername.append(username.charAt(0));
	            for (int i = 1; i < username.length() - 1; i++) {
	                maskedUsername.append("*");
	            }
	            if (username.length() > 1) {
	                maskedUsername.append(username.charAt(username.length() - 1));
	            }
	        } else {
	            maskedUsername.append(username, 0, startChars); // first `startChars` characters
	            for (int i = 0; i < username.length() - startChars - endChars; i++) {
	                maskedUsername.append("*");
	            }
	            maskedUsername.append(username, username.length() - endChars, username.length()); // last `endChars` characters
	        }

	        // Return the masked email
	        return maskedUsername.toString() + "@" + domain;
	    }
}
