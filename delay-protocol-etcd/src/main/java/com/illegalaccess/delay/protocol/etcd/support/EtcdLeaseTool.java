package com.illegalaccess.delay.protocol.etcd.support;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * 操作etcd leaseId的工具
 * @date 2021-03-04 10:29
 * @author Jimmy Li
 */
@Slf4j
public class EtcdLeaseTool {

    /**
     * 存储 leaseId
     * @param path
     * @param leaseId
     * @return
     */
    public static boolean writeLeaseInfo(String path, Long leaseId) {
        try {
            Files.write(Paths.get(path), leaseId.toString().getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            log.error("write lease:{} to disk fail", leaseId, e);
            return false;
        }

        return true;
    }

    /**
     *
     * @param path
     * @return
     */
    public static long getLeaseExpireTime(String path) {
        Path filePath = Paths.get(path);
        if (Files.notExists(filePath)) {
            return 0L;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            String data = lines.get(0);
            return Long.parseLong(data);
        } catch (IOException e) {
            log.error("get lease expire time fail", e);
            return 0L;
        }
    }
}
