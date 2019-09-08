package org.levy.filecollector.utils;

import org.levy.filecollector.entity.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    public static Result<Map<String, Boolean>> scanDir(String path) {
        Map<String, Boolean> resultSet = new HashMap<>();
        try {
            Files.list(Paths.get(path)).forEach(s -> resultSet.put(s.toString(), s.toFile().isFile()));
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
        return Result.success(resultSet);
    }

    public static boolean createDirRecursive(File targetDir) {
        return targetDir.exists() || (createDirRecursive(targetDir.getParentFile()) && targetDir.mkdir());
    }
}
