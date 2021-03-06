package home.edu.vn.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEmailFormating {
	
	private Pattern pattern;
	private Matcher matcher;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public CheckEmailFormating()
	{
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public boolean checkEmailValid(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
	
	
	
}
