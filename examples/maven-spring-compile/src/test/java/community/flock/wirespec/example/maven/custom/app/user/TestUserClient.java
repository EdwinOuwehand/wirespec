package community.flock.wirespec.example.maven.custom.app.user;

import community.flock.wirespec.generated.java.DeleteUserByNameEndpoint;
import community.flock.wirespec.generated.java.GetUserByNameEndpoint;
import community.flock.wirespec.generated.java.GetUsersEndpoint;
import community.flock.wirespec.generated.java.PostUserEndpoint;
import community.flock.wirespec.generated.java.UploadImageEndpoint;
import community.flock.wirespec.generated.java.UserDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class TestUserClient implements UserClient {

    public static final Set<UserDto> users = new HashSet<>(Set.of(
            new UserDto("name"),
            new UserDto("other name")
    ));

    public static final Map<String, byte[]> images = new HashMap<>();

    @Override
    public CompletableFuture<GetUsersEndpoint.Response<?>> getUsers(GetUsersEndpoint.Request request) {
        var filtered = users.stream().filter(it -> Objects.equals(it.name(), request.getQueries().name())).toList();
        return completedFuture(new GetUsersEndpoint.Response200(filtered));
    }

    @Override
    public CompletableFuture<GetUserByNameEndpoint.Response<?>> getUserByName(GetUserByNameEndpoint.Request request) {
        var res = users.stream()
                .filter(it -> Objects.equals(it.name(), request.getPath().name()))
                .findFirst()
                .<GetUserByNameEndpoint.Response<?>>map(GetUserByNameEndpoint.Response200::new)
                .orElseGet(() -> new GetUserByNameEndpoint.Response404());

        return completedFuture(res);
    }

    @Override
    public CompletableFuture<PostUserEndpoint.Response<?>> postUser(PostUserEndpoint.Request request) {
        var user = request.getBody();
        if (users.add(user)) {
            return completedFuture(new PostUserEndpoint.Response200(user));
        } else {
            return completedFuture(new PostUserEndpoint.Response409());
        }
    }

    @Override
    public CompletableFuture<DeleteUserByNameEndpoint.Response<?>> deleteUserByName(DeleteUserByNameEndpoint.Request request) {
        var res = users.stream()
                .filter(it -> Objects.equals(it.name(), request.getPath().name()))
                .findFirst()
                .<DeleteUserByNameEndpoint.Response<?>>map(body -> {
                    users.remove(body);
                    return new DeleteUserByNameEndpoint.Response200(body);
                })
                .orElseGet(() -> new DeleteUserByNameEndpoint.Response404());

        return completedFuture(res);
    }

    @Override
    public CompletableFuture<UploadImageEndpoint.Response<?>> uploadImage(UploadImageEndpoint.Request request) {
        images.put(request.getPath().name(),  request.getBody());
        return completedFuture(new UploadImageEndpoint.Response201());
    }
}
