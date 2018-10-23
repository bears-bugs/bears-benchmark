package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.io.sequence.fasta.FastaWriter;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.util.RandomUtil;
import com.milaboratory.util.TempFileManager;
import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BlastDBBuilder {
    private static final String RECORD_PREFIX = "RECORD";
    private static Path blastDbFolder = null;

    private static synchronized Path getTmpDBPath() {
        if (blastDbFolder == null)
            blastDbFolder = TempFileManager.getTempDir().toPath();
        return blastDbFolder;
    }

    public static String getIdFasta(int id) {
        return "lcl|" + RECORD_PREFIX + id + "|";
    }

    public static String getIdKey(int id) {
        return RECORD_PREFIX + id;
    }

    public static <S extends Sequence<S>> BlastDB build(List<S> sequences) {
        return build(sequences, false);
    }

    public static <S extends Sequence<S>> BlastDB build(List<S> sequences, boolean buildIdIndex) {
        return build(sequences, buildIdIndex, null, true);
    }

    //TODO caching etc..
    private static <S extends Sequence<S>> BlastDB build(List<S> sequences, boolean buildIdIndex,
                                                         Path path, boolean tmp) {
        if (sequences.isEmpty())
            throw new IllegalArgumentException("No records.");

        if (path == null)
            path = getTmpDBPath();

        Alphabet<S> alphabet = sequences.get(0).getAlphabet();

        try {
            String name = RandomUtil.getThreadLocalRandomData().nextHexString(40);
            String fullName = path.resolve(name).toString();
            List<String> cmd = new ArrayList<>();
            cmd.addAll(Arrays.asList(Blast.CMD_MAKEBLASTDB, "-dbtype", Blast.toBlastAlphabet(alphabet),
                    "-out", fullName, "-title", name));
            cmd.addAll(Arrays.asList("-parse_seqids", "-hash_index"));
            Process proc = Blast.getProcessBuilder(cmd).start();
            FastaWriter<S> writer = new FastaWriter<>(proc.getOutputStream(), FastaWriter.DEFAULT_MAX_LENGTH);
            for (int i = 0; i < sequences.size(); i++)
                writer.write(getIdFasta(i), sequences.get(i));
            writer.close();
            String err = IOUtils.toString(proc.getErrorStream(), Charset.forName("US-ASCII"));
            if (proc.waitFor() != 0)
                throw new RuntimeException("Something goes wrong: " + err);
            return BlastDB.get(fullName, tmp);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
