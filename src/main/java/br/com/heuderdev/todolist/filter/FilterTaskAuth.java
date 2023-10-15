package br.com.heuderdev.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.heuderdev.todolist.repositories.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var serveletPath = request.getServletPath();

        if (serveletPath.equals("/tasks")) {
            // Pegar a autenticação (username e password)
            var authorization = request.getHeader("Authorization");
            var user_password = authorization.substring("Basic".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(user_password);
            var authString = new String(authDecode);
            String[] credentials = authString.split(":");
            var username = credentials[0];
            var password = credentials[1];


            var userExists = this.repository.findByUsername(username);

            if (userExists == null) {
                response.sendError(401);
            } else {
                // validar usuário
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), userExists.getPassword());

                if (passwordVerify.verified) {
                    request.setAttribute("user_id", userExists.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }


            }

        } else {
            filterChain.doFilter(request,response);
        }

    }
}
