package yaml.parser;

import java.io.*;

public final class ParserReader
{
    Reader reader;
    int c;
    char[] buffer;
    int index;
    int fileIndex;
    int level;
    int eofIndex;
    int[] mark;
    final int BUFLEN = 120000;
    
    public ParserReader(final Reader reader) {
        this.reader = reader;
        (this.buffer = new char[120000])[this.buffer.length - 1] = '\0';
        this.index = 0;
        this.fileIndex = 0;
        this.level = 0;
        this.eofIndex = -1;
        this.mark = new int[1000];
    }
    
    public String string() {
        final int n = this.mark[this.level - 1];
        final int index = this.index;
        if (n > index) {
            return new String(this.buffer, n, 120000 - n) + new String(this.buffer, 0, index);
        }
        return new String(this.buffer, n, index - n);
    }
    
    public int read() throws IOException {
        if (this.index == this.eofIndex) {
            ++this.index;
            return -1;
        }
        if (this.index != this.fileIndex % 120000) {
            this.c = this.buffer[this.index];
        }
        else {
            if (this.eofIndex != -1) {
                return -1;
            }
            this.c = this.reader.read();
            ++this.fileIndex;
            if (this.c == -1) {
                this.eofIndex = this.index;
            }
            this.buffer[this.index] = (char)this.c;
        }
        ++this.index;
        if (this.index >= 120000) {
            this.index = 0;
        }
        return this.c;
    }
    
    public int current() throws IOException {
        this.read();
        this.unread();
        return this.c;
    }
    
    public int previous() {
        if (this.index == 0) {
            return this.buffer[119998];
        }
        if (this.index == 1) {
            return this.buffer[119999];
        }
        return this.buffer[this.index - 2];
    }
    
    public void mark() {
        this.mark[this.level] = this.index;
        ++this.level;
    }
    
    public void unmark() {
        --this.level;
        if (this.level < 0) {
            throw new IndexOutOfBoundsException("no more mark()'s to unmark()");
        }
    }
    
    public void reset() {
        this.unmark();
        this.index = this.mark[this.level];
    }
    
    public void unread() {
        --this.index;
        if (this.index < 0) {
            this.index = 119999;
        }
    }
}
