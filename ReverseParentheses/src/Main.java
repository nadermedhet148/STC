import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        System.out.println(reverseParentheses("dd(df)a(ghhh)"));
        System.out.println(reverseParentheses("abd(jnb)asdf"));

    }

    public static String reverseParentheses(String s) {
        Stack<StringBuilder> st = new Stack<>();
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                st.push(sb);
                sb = new StringBuilder().append(')');
            } else if (c == ')') {
                String str = sb.reverse().toString();
                sb = st.pop();
                sb.append('(').append(str);

            } else sb.append(c);
        }
        return sb.toString();
    }

}