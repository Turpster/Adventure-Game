package yaml.parser;

import java.util.*;
import org.ho.yaml.*;
import java.io.*;
import org.ho.yaml.tests.*;

public final class YamlParser
{
    public static final int LIST_OPEN = 91;
    public static final int LIST_CLOSE = 93;
    public static final int MAP_OPEN = 123;
    public static final int MAP_CLOSE = 125;
    public static final int LIST_NO_OPEN = 110;
    public static final int MAP_NO_OPEN = 78;
    public static final int DOCUMENT_HEADER = 72;
    public static final int MAP_SEPARATOR = 58;
    public static final int LIST_ENTRY = 45;
    protected ParserReader r;
    protected int line;
    private ParserEvent event;
    private HashMap props;
    private char pendingEvent;
    
    public YamlParser(final Reader reader, final ParserEvent event) {
        this.line = 1;
        this.r = new ParserReader(reader);
        this.event = event;
        this.props = new HashMap();
    }
    
    protected String readerString() {
        return this.r.string();
    }
    
    private void clearEvents() {
        this.props.clear();
    }
    
    private void sendEvents() {
        if (this.pendingEvent == '[') {
            this.event.event(91);
        }
        if (this.pendingEvent == '{') {
            this.event.event(123);
        }
        this.pendingEvent = '\0';
        final String s;
        if ((s = this.props.get("anchor")) != null) {
            this.event.property("anchor", s);
        }
        final String s2;
        if ((s2 = this.props.get("transfer")) != null) {
            this.event.property("transfer", s2);
        }
        final String s3;
        if ((s3 = this.props.get("alias")) != null) {
            this.event.content("alias", s3);
        }
        if (this.props.keySet().contains("string")) {
            this.event.content("string", this.props.get("string"));
        }
        if (this.props.keySet().contains("value")) {
            this.event.content("value", this.props.get("value"));
        }
        this.props.clear();
    }
    
    public int indent() throws IOException, SyntaxException {
        this.mark();
        int n = 0;
        int read;
        while (YamlCharacter.is(read = this.r.read(), 8)) {
            ++n;
        }
        if (read == 9) {
            throw new SyntaxException("Tabs may not be used for indentation.", this.line);
        }
        this.reset();
        return n;
    }
    
    public boolean array(final int n) throws IOException {
        this.mark();
        int n2 = 0;
        while (YamlCharacter.is(this.r.read(), n)) {
            ++n2;
        }
        if (n2 != 0) {
            this.r.unread();
            this.unmark();
            return true;
        }
        this.reset();
        return false;
    }
    
    public boolean space() throws IOException {
        return this.array(5);
    }
    
    public boolean line() throws IOException {
        return this.array(3);
    }
    
    public boolean linesp() throws IOException {
        return this.array(4);
    }
    
    public boolean word() throws IOException {
        return this.array(2);
    }
    
    public boolean number() throws IOException {
        return this.array(7);
    }
    
    public boolean indent(int n) throws IOException {
        this.mark();
        while (YamlCharacter.is(this.r.read(), 8) && n > 0) {
            --n;
        }
        if (n == 0) {
            this.r.unread();
            this.unmark();
            return true;
        }
        this.reset();
        return false;
    }
    
    public boolean newline() throws IOException {
        ++this.line;
        this.mark();
        final int read = this.r.read();
        final int read2 = this.r.read();
        if (read == -1 || (read == 13 && read2 == 10)) {
            this.unmark();
            return true;
        }
        if (YamlCharacter.is(read, 6)) {
            this.r.unread();
            this.unmark();
            return true;
        }
        this.reset();
        --this.line;
        return false;
    }
    
    public boolean end() throws IOException, SyntaxException {
        this.mark();
        this.space();
        if (!this.newline()) {
            this.reset();
            return false;
        }
        while (this.comment(-1, false)) {}
        this.unmark();
        return true;
    }
    
    public boolean string_simple() throws IOException {
        int n = 0;
        this.r.mark();
        boolean b = false;
        while (true) {
            final int read = this.r.read();
            if (read == -1) {
                break;
            }
            final char c = (char)read;
            if (n == 0 && '-' == c) {
                b = true;
            }
            else {
                if (n == 0) {
                    if (YamlCharacter.isSpaceChar(c) || YamlCharacter.isIndicatorNonSpace(c)) {
                        break;
                    }
                    if (YamlCharacter.isIndicatorSpace(c)) {
                        break;
                    }
                }
                if (b && (YamlCharacter.isSpaceChar(c) || YamlCharacter.isLineBreakChar(c))) {
                    this.unmark();
                    return false;
                }
                if (!YamlCharacter.isLineSpChar(c)) {
                    break;
                }
                if (YamlCharacter.isIndicatorSimple(c) && this.r.previous() != 92) {
                    break;
                }
                ++n;
            }
        }
        this.r.unread();
        this.r.unmark();
        return n != 0;
    }
    
    public boolean loose_string_simple() throws IOException {
        char c = '\0';
        int n = 0;
        while (true) {
            final int read = this.r.read();
            if (read == -1) {
                break;
            }
            c = (char)read;
            if (!YamlCharacter.isLineSpChar(c)) {
                break;
            }
            if (YamlCharacter.isLooseIndicatorSimple(c) && this.r.previous() != 92) {
                break;
            }
            ++n;
        }
        this.r.unread();
        return n != 0 || YamlCharacter.isLineBreakChar(c);
    }
    
    boolean string_q1() throws IOException, SyntaxException {
        if (this.r.current() != 39) {
            return false;
        }
        this.r.read();
        int n = 0;
        int read;
        while (YamlCharacter.is(read = this.r.read(), 4) && (read != 39 || this.r.previous() == 92)) {
            ++n;
        }
        if (read != 39) {
            throw new SyntaxException("Unterminated string", this.line);
        }
        return true;
    }
    
    boolean string_q2() throws IOException, SyntaxException {
        if (this.r.current() != 34) {
            return false;
        }
        this.r.read();
        int n = 0;
        int read;
        while (YamlCharacter.is(read = this.r.read(), 4) && (read != 34 || this.r.previous() == 92)) {
            ++n;
        }
        if (read != 34) {
            throw new SyntaxException("Unterminated string", this.line);
        }
        return true;
    }
    
    public boolean loose_string() throws IOException, SyntaxException {
        this.mark();
        boolean string_q2 = false;
        final boolean string_q3;
        if ((string_q3 = this.string_q1()) || (string_q2 = this.string_q2()) || this.loose_string_simple()) {
            String s = this.r.string().trim();
            if (string_q2) {
                s = this.fix_q2(s);
            }
            else if (string_q3) {
                s = this.fix_q1(s);
            }
            if (string_q3 || string_q2) {
                this.props.put("string", s);
            }
            else if ("".equals(s)) {
                this.props.put("value", null);
            }
            else {
                this.props.put("value", s);
            }
            this.unmark();
            return true;
        }
        this.reset();
        return false;
    }
    
    public boolean string() throws IOException, SyntaxException {
        this.mark();
        boolean string_q2 = false;
        final boolean string_q3;
        if ((string_q3 = this.string_q1()) || (string_q2 = this.string_q2()) || this.string_simple()) {
            String s = this.r.string().trim();
            if (string_q2) {
                s = this.fix_q2(s);
            }
            else if (string_q3) {
                s = this.fix_q1(s);
            }
            if (string_q3 || string_q2) {
                this.props.put("string", s);
            }
            else {
                this.props.put("value", s);
            }
            this.unmark();
            return true;
        }
        this.reset();
        return false;
    }
    
    String fix_q2(final String s) {
        if (s.length() > 2) {
            return Utilities.unescape(s.substring(1, s.length() - 1));
        }
        return "";
    }
    
    String fix_q1(final String s) {
        if (s.length() > 2) {
            return s.substring(1, s.length() - 1);
        }
        return "";
    }
    
    public boolean alias() throws IOException {
        this.mark();
        if (this.r.read() != 42) {
            this.r.unread();
            this.unmark();
            return false;
        }
        if (!this.word()) {
            this.reset();
            return false;
        }
        this.unmark();
        this.props.put("alias", this.r.string());
        return true;
    }
    
    public boolean anchor() throws IOException {
        this.mark();
        if (this.r.read() != 38) {
            this.r.unread();
            this.unmark();
            return false;
        }
        if (!this.word()) {
            this.reset();
            return false;
        }
        this.unmark();
        this.props.put("anchor", this.r.string());
        return true;
    }
    
    public boolean comment(final int n, final boolean b) throws IOException, SyntaxException {
        this.mark();
        if (n != -1 && this.indent() >= n) {
            this.reset();
            return false;
        }
        this.space();
        final int read;
        if ((read = this.r.read()) == 35) {
            this.linesp();
        }
        else {
            if (read == -1) {
                this.unmark();
                return false;
            }
            if (b) {
                this.reset();
                return false;
            }
            this.r.unread();
        }
        if (!this.newline()) {
            this.reset();
            return false;
        }
        this.unmark();
        return true;
    }
    
    public boolean header() throws IOException {
        this.mark();
        final int read = this.r.read();
        final int read2 = this.r.read();
        final int read3 = this.r.read();
        if (read != 45 || read2 != 45 || read3 != 45) {
            this.reset();
            return false;
        }
        while (this.space() && this.directive()) {}
        this.unmark();
        this.event.event(72);
        return true;
    }
    
    public boolean directive() throws IOException {
        this.mark();
        if (this.r.read() != 35) {
            this.r.unread();
            this.unmark();
            return false;
        }
        if (!this.word()) {
            this.reset();
            return false;
        }
        if (this.r.read() != 58) {
            this.reset();
            return false;
        }
        if (!this.line()) {
            this.reset();
            return false;
        }
        this.event.content("directive", this.r.string());
        this.unmark();
        return true;
    }
    
    public boolean transfer() throws IOException {
        this.mark();
        if (this.r.read() != 33) {
            this.r.unread();
            this.unmark();
            return false;
        }
        if (!this.line()) {
            this.reset();
            return false;
        }
        this.props.put("transfer", this.r.string());
        this.unmark();
        return true;
    }
    
    public boolean properties() throws IOException {
        this.mark();
        if (this.transfer()) {
            this.space();
            this.anchor();
            this.unmark();
            return true;
        }
        if (this.anchor()) {
            this.space();
            this.transfer();
            this.unmark();
            return true;
        }
        this.reset();
        return false;
    }
    
    public boolean key(final int n) throws IOException, SyntaxException {
        if (this.r.current() == 63) {
            this.r.read();
            if (!this.value_nested(n + 1)) {
                throw new SyntaxException("'?' key indicator without a nested value", this.line);
            }
            if (!this.indent(n)) {
                throw new SyntaxException("Incorrect indentations after nested key", this.line);
            }
            return true;
        }
        else {
            if (!this.value_inline()) {
                return false;
            }
            this.space();
            return true;
        }
    }
    
    public boolean value(final int n) throws IOException, SyntaxException {
        if (this.value_nested(n) || this.value_block(n)) {
            return true;
        }
        if (!this.loose_value_inline()) {
            return false;
        }
        if (!this.end()) {
            throw new SyntaxException("Unterminated inline value", this.line);
        }
        return true;
    }
    
    public boolean loose_value(final int n) throws IOException, SyntaxException {
        if (this.value_nested(n) || this.value_block(n)) {
            return true;
        }
        if (!this.loose_value_inline()) {
            return false;
        }
        if (!this.end()) {
            throw new SyntaxException("Unterminated inline value", this.line);
        }
        return true;
    }
    
    public boolean value_na(final int n) throws IOException, SyntaxException {
        if (this.value_nested(n) || this.value_block(n)) {
            return true;
        }
        if (!this.value_inline_na()) {
            return false;
        }
        if (!this.end()) {
            throw new SyntaxException("Unterminated inline value", this.line);
        }
        return true;
    }
    
    public boolean value_inline() throws IOException, SyntaxException {
        this.mark();
        if (this.properties()) {
            this.space();
        }
        if (this.alias() || this.string()) {
            this.sendEvents();
            this.unmark();
            return true;
        }
        if (this.list() || this.map()) {
            this.unmark();
            return true;
        }
        this.clearEvents();
        this.reset();
        return false;
    }
    
    public boolean loose_value_inline() throws IOException, SyntaxException {
        this.mark();
        if (this.properties()) {
            this.space();
        }
        if (this.alias() || this.loose_string()) {
            this.sendEvents();
            this.unmark();
            return true;
        }
        if (this.list() || this.map()) {
            this.unmark();
            return true;
        }
        this.clearEvents();
        this.reset();
        return false;
    }
    
    public boolean value_inline_na() throws IOException, SyntaxException {
        this.mark();
        if (this.properties()) {
            this.space();
        }
        if (this.string()) {
            this.sendEvents();
            this.unmark();
            return true;
        }
        if (this.list() || this.map()) {
            this.unmark();
            return true;
        }
        this.clearEvents();
        this.reset();
        return false;
    }
    
    public boolean value_nested(final int n) throws IOException, SyntaxException {
        this.mark();
        if (this.properties()) {
            this.space();
        }
        if (!this.end()) {
            this.clearEvents();
            this.reset();
            return false;
        }
        this.sendEvents();
        while (this.comment(n, false)) {}
        if (this.nlist(n) || this.nmap(n)) {
            this.unmark();
            return true;
        }
        this.reset();
        return false;
    }
    
    public boolean value_block(final int n) throws IOException, SyntaxException {
        this.mark();
        if (this.properties()) {
            this.space();
        }
        if (!this.block(n)) {
            this.clearEvents();
            this.reset();
            return false;
        }
        this.sendEvents();
        while (this.comment(n, false)) {}
        this.unmark();
        return true;
    }
    
    public boolean nmap(int n) throws IOException, SyntaxException {
        this.mark();
        final int indent = this.indent();
        if (n == -1) {
            n = indent;
        }
        else if (indent > n) {
            n = indent;
        }
        this.pendingEvent = '{';
        int n2 = 0;
        while (true) {
            while (this.indent(n)) {
                if (!this.nmap_entry(n)) {
                    if (n2 > 0) {
                        this.event.event(125);
                        this.unmark();
                        return true;
                    }
                    this.pendingEvent = '\0';
                    this.reset();
                    return false;
                }
                else {
                    ++n2;
                }
            }
            continue;
        }
    }
    
    public boolean nmap_entry(final int n) throws IOException, SyntaxException {
        if (!this.key(n)) {
            return false;
        }
        if (this.r.current() != 58) {
            return false;
        }
        this.r.read();
        this.event.event(58);
        this.space();
        if (!this.loose_value(n + 1)) {
            throw new SyntaxException("no value after ':'", this.line);
        }
        return true;
    }
    
    public boolean nlist(int n) throws IOException, SyntaxException {
        this.mark();
        final int indent = this.indent();
        if (n == -1) {
            n = indent;
        }
        else if (indent > n) {
            n = indent;
        }
        this.pendingEvent = '[';
        int n2 = 0;
        while (true) {
            while (this.indent(n)) {
                if (!this.nlist_entry(n)) {
                    if (n2 > 0) {
                        this.event.event(93);
                        this.unmark();
                        return true;
                    }
                    this.pendingEvent = '\0';
                    this.reset();
                    return false;
                }
                else {
                    ++n2;
                }
            }
            continue;
        }
    }
    
    boolean start_list() throws IOException {
        this.r.mark();
        if (this.r.read() == 45 && (YamlCharacter.isLineBreakChar((char)this.r.current()) || this.space())) {
            this.r.unmark();
            return true;
        }
        this.r.reset();
        return false;
    }
    
    public boolean nlist_entry(final int n) throws IOException, SyntaxException {
        if (!this.start_list()) {
            return false;
        }
        this.space();
        if (this.nmap_inlist(n + 1) || this.value(n + 1)) {
            return true;
        }
        throw new SyntaxException("bad nlist", this.line);
    }
    
    public boolean nmap_inlist(int n) throws IOException, SyntaxException {
        this.mark();
        if (!this.string()) {
            this.reset();
            return false;
        }
        this.space();
        if (this.r.read() != 58) {
            this.reset();
            return false;
        }
        if (this.pendingEvent == '[') {
            this.event.event(91);
            this.pendingEvent = '\0';
        }
        this.event.event(123);
        this.sendEvents();
        this.event.event(58);
        if (!this.space()) {
            this.reset();
            return false;
        }
        if (!this.value(n + 1)) {
            throw new SyntaxException("No value after ':' in map_in_list", this.line);
        }
        ++n;
        final int indent = this.indent();
        if (n == -1) {
            n = indent;
        }
        else if (indent > n) {
            n = indent;
        }
        int n2 = 0;
        while (true) {
            while (this.indent(n)) {
                if (!this.nmap_entry(n)) {
                    this.event.event(125);
                    this.unmark();
                    return true;
                }
                ++n2;
            }
            continue;
        }
    }
    
    public boolean block(final int n) throws IOException, SyntaxException {
        final int current = this.r.current();
        if (current != 124 && current != 93 && current != 62) {
            return false;
        }
        this.r.read();
        if (this.r.current() == 92) {
            this.r.read();
        }
        this.space();
        if (this.number()) {
            this.space();
        }
        if (!this.newline()) {
            throw new SyntaxException("No newline after block definition", this.line);
        }
        final StringBuffer sb = new StringBuffer();
        while (-1 != this.block_line(n, this.block_line(n, -1, sb, (char)current), sb, (char)current)) {}
        String s = sb.toString();
        if (s.length() > 0 && YamlCharacter.isLineBreakChar(s.charAt(s.length() - 1))) {
            s = s.substring(0, s.length() - 1);
        }
        this.event.content("string", s);
        return true;
    }
    
    public int block_line(int n, final int n2, final StringBuffer sb, final char c) throws IOException, SyntaxException {
        int indent;
        if (n2 == -1) {
            indent = this.indent();
            if (indent < n) {
                return -1;
            }
            n = indent;
            this.indent(n);
        }
        else {
            indent = n2;
            if (!this.indent(n2)) {
                return -1;
            }
        }
        if (this.r.current() == -1) {
            return -1;
        }
        this.mark();
        this.linesp();
        sb.append(this.r.string());
        this.unmark();
        if (c == '|') {
            sb.append('\n');
        }
        else {
            sb.append(' ');
        }
        this.newline();
        return indent;
    }
    
    public boolean list() throws IOException, SyntaxException {
        if (this.r.current() != 91) {
            return false;
        }
        this.r.read();
        this.sendEvents();
        this.event.event(91);
        while (this.list_entry()) {
            final int current = this.r.current();
            if (current == 93) {
                this.r.read();
                this.event.event(93);
                return true;
            }
            if (current != 44) {
                throw new SyntaxException("inline list error: expecting ','", this.line);
            }
            this.r.read();
        }
        if (this.r.current() == 93) {
            this.r.read();
            this.event.event(93);
            return true;
        }
        throw new SyntaxException("inline list error", this.line);
    }
    
    public boolean list_entry() throws IOException, SyntaxException {
        this.space();
        if (!this.loose_value_inline()) {
            return false;
        }
        this.space();
        return true;
    }
    
    public boolean map() throws IOException, SyntaxException {
        if (this.r.current() != 123) {
            return false;
        }
        this.r.read();
        this.sendEvents();
        this.event.event(123);
        while (this.map_entry()) {
            final int current = this.r.current();
            if (current == 125) {
                this.r.read();
                this.event.event(125);
                return true;
            }
            if (current != 44) {
                throw new SyntaxException("inline map error: expecting ','", this.line);
            }
            this.r.read();
        }
        if (this.r.current() == 125) {
            this.r.read();
            this.event.event(125);
            return true;
        }
        throw new SyntaxException("inline map error", this.line);
    }
    
    public boolean map_entry() throws IOException, SyntaxException {
        this.space();
        if (!this.value_inline()) {
            return false;
        }
        this.space();
        if (this.r.current() != 58) {
            return false;
        }
        this.r.read();
        this.event.event(58);
        if (!this.space()) {
            throw new SyntaxException("No space after ':'", this.line);
        }
        if (!this.loose_value_inline()) {
            throw new SyntaxException("No value after ':'", this.line);
        }
        this.space();
        return true;
    }
    
    public boolean document_first() throws IOException, SyntaxException {
        final boolean b = this.nlist(-1) || this.nmap(-1);
        this.mark();
        if (!this.header() && this.r.read() != -1 && this.r.read() != -1) {
            throw new SyntaxException("End of document expected.");
        }
        this.unmark();
        if (!b) {
            throw new SyntaxException("first document is not a nested list or map", this.line);
        }
        return true;
    }
    
    public boolean document_next() throws IOException, SyntaxException {
        return this.header() && this.value_na(-1);
    }
    
    public void parse() throws IOException, SyntaxException {
        try {
            while (this.comment(-1, false)) {}
            if (!this.header()) {
                this.document_first();
            }
            else {
                this.value_na(-1);
            }
            while (this.document_next()) {}
        }
        catch (SyntaxException ex) {
            this.event.error(ex, ex.line);
        }
    }
    
    private void mark() {
        this.r.mark();
    }
    
    private void reset() {
        this.r.reset();
    }
    
    private void unmark() {
        this.r.unmark();
    }
    
    public ParserEvent getEvent() {
        return this.event;
    }
    
    public void setEvent(final ParserEvent event) {
        this.event = event;
    }
    
    public int getLineNumber() {
        return this.line;
    }
    
    public static void parse(final File file) throws FileNotFoundException {
        parse(new FileReader(file));
    }
    
    public static void parse(final String s) {
        parse(new StringReader(s));
    }
    
    public static void parse(final Reader reader) {
        try {
            new YamlParser(reader, (ParserEvent)new TestYamlParserEvent()).parse();
            reader.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        catch (SyntaxException ex2) {
            throw new RuntimeException(ex2);
        }
    }
}
