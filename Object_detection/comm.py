import socket
import select

class Comm:
    HP_TUP = ("localhost", 65432)

    def __init__(self):
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.bind(self.HP_TUP)
        self.s.listen(1)
        print("Waiting for connection")
        self.conn, self.addr = self.s.accept()
        print("Connected")

    def send(self, data):
        self.conn.setblocking(True)
        self.conn.send(data)
        self.conn.setblocking(False)

    def recv(self):
        self.conn.setblocking(True)
        data = self.conn.recv(4096)
        self.conn.setblocking(False)
        return data

    def is_ready(self):
       return select.select([self.conn], [], [], 10)[0]

    def close(self):
        self.conn.shutdown(socket.SHUT_RDWR)
        self.conn.close()
        self.s.shutdown(socket.SHUT_RDWR)
        self.s.close()
