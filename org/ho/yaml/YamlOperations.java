package org.ho.yaml;

import java.util.*;
import java.io.*;

public interface YamlOperations
{
    Object load(final InputStream p0);
    
    Object load(final Reader p0);
    
    Object load(final File p0) throws FileNotFoundException;
    
    Object load(final String p0);
    
     <T> T loadType(final InputStream p0, final Class<T> p1);
    
     <T> T loadType(final Reader p0, final Class<T> p1);
    
     <T> T loadType(final File p0, final Class<T> p1) throws FileNotFoundException;
    
     <T> T loadType(final String p0, final Class<T> p1);
    
    YamlStream loadStream(final InputStream p0);
    
    YamlStream loadStream(final Reader p0);
    
    YamlStream loadStream(final File p0) throws FileNotFoundException;
    
    YamlStream loadStream(final String p0);
    
     <T> YamlStream<T> loadStreamOfType(final InputStream p0, final Class<T> p1);
    
     <T> YamlStream<T> loadStreamOfType(final Reader p0, final Class<T> p1);
    
     <T> YamlStream<T> loadStreamOfType(final File p0, final Class<T> p1) throws FileNotFoundException;
    
     <T> YamlStream<T> loadStreamOfType(final String p0, final Class<T> p1);
    
    void dump(final Object p0, final File p1) throws FileNotFoundException;
    
    void dump(final Object p0, final File p1, final boolean p2) throws FileNotFoundException;
    
    String dump(final Object p0);
    
    String dump(final Object p0, final boolean p1);
    
    void dumpStream(final Iterator p0, final File p1) throws FileNotFoundException;
    
    void dumpStream(final Iterator p0, final File p1, final boolean p2) throws FileNotFoundException;
    
    String dumpStream(final Iterator p0);
    
    String dumpStream(final Iterator p0, final boolean p1);
    
    void dump(final Object p0, final OutputStream p1);
    
    void dump(final Object p0, final OutputStream p1, final boolean p2);
}
