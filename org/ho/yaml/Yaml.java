package org.ho.yaml;

import java.util.*;
import java.io.*;

public class Yaml
{
    public static YamlConfig config;
    
    public static Object load(final Reader reader) {
        return Yaml.config.load(reader);
    }
    
    public static Object load(final InputStream inputStream) {
        return Yaml.config.load(inputStream);
    }
    
    public static Object load(final File file) throws FileNotFoundException {
        return Yaml.config.load(file);
    }
    
    public static Object load(final String s) {
        return Yaml.config.load(s);
    }
    
    public static <T> T loadType(final Reader reader, final Class<T> clazz) {
        return Yaml.config.loadType(reader, clazz);
    }
    
    public static <T> T loadType(final InputStream inputStream, final Class<T> clazz) throws FileNotFoundException {
        return Yaml.config.loadType(inputStream, clazz);
    }
    
    public static <T> T loadType(final File file, final Class<T> clazz) throws FileNotFoundException {
        return Yaml.config.loadType(file, clazz);
    }
    
    public static <T> T loadType(final String s, final Class<T> clazz) {
        return Yaml.config.loadType(s, clazz);
    }
    
    public static YamlStream loadStream(final Reader reader) {
        return Yaml.config.loadStream(reader);
    }
    
    public static YamlStream loadStream(final InputStream inputStream) {
        return Yaml.config.loadStream(inputStream);
    }
    
    public static YamlStream loadStream(final File file) throws FileNotFoundException {
        return Yaml.config.loadStream(file);
    }
    
    public static YamlStream loadStream(final String s) {
        return Yaml.config.loadStream(s);
    }
    
    public static <T> YamlStream<T> loadStreamOfType(final Reader reader, final Class<T> clazz) {
        return Yaml.config.loadStreamOfType(reader, clazz);
    }
    
    public static <T> YamlStream<T> loadStreamOfType(final InputStream inputStream, final Class<T> clazz) {
        return Yaml.config.loadStreamOfType(inputStream, clazz);
    }
    
    public static <T> YamlStream<T> loadStreamOfType(final File file, final Class<T> clazz) throws FileNotFoundException {
        return Yaml.config.loadStreamOfType(file, clazz);
    }
    
    public static <T> YamlStream<T> loadStreamOfType(final String s, final Class<T> clazz) throws FileNotFoundException {
        return Yaml.config.loadStreamOfType(s, clazz);
    }
    
    public static void dump(final Object o, final File file) throws FileNotFoundException {
        Yaml.config.dump(o, file);
    }
    
    public static void dump(final Object o, final File file, final boolean b) throws FileNotFoundException {
        Yaml.config.dump(o, file, b);
    }
    
    public static void dumpStream(final Iterator iterator, final File file, final boolean b) throws FileNotFoundException {
        Yaml.config.dumpStream(iterator, file, b);
    }
    
    public static void dumpStream(final Iterator iterator, final File file) throws FileNotFoundException {
        Yaml.config.dumpStream(iterator, file);
    }
    
    public static String dump(final Object o) {
        return Yaml.config.dump(o);
    }
    
    public static String dump(final Object o, final boolean b) {
        return Yaml.config.dump(o, b);
    }
    
    public static String dumpStream(final Iterator iterator) {
        return Yaml.config.dumpStream(iterator);
    }
    
    public static String dumpStream(final Iterator iterator, final boolean b) {
        return Yaml.config.dumpStream(iterator, b);
    }
    
    public static void dump(final Object o, final OutputStream outputStream) {
        Yaml.config.dump(o, outputStream);
    }
    
    public static void dump(final Object o, final OutputStream outputStream, final boolean b) {
        Yaml.config.dump(o, outputStream, b);
    }
    
    static {
        Yaml.config = YamlConfig.getDefaultConfig();
    }
}
