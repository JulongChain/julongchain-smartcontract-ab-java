package org.bcia.javachain.common.ssh;

import org.junit.Test;

public class SSHDockerTest {

    @Test
    public void executeCommand() {
        String command = "docker build -t docker.bcia.javachain.com:5000/jdk.ubuntu /root/docker/jdk.ubuntu";
        SSHDocker.executeCommand("192.168.246.130",22,"root","000000",command);
    }
}