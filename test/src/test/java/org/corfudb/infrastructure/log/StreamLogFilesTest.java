package org.corfudb.infrastructure.log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.corfudb.infrastructure.log.StreamLogFiles.METADATA_SIZE;
import static org.corfudb.infrastructure.log.StreamLogFiles.RECORDS_PER_LOG_FILE;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.netty.buffer.Unpooled;
import org.apache.commons.io.FileUtils;
import org.corfudb.AbstractCorfuTest;
import org.corfudb.format.Types.Metadata;
import org.corfudb.infrastructure.ServerContext;
import org.corfudb.infrastructure.ServerContextBuilder;
import org.corfudb.protocols.wireprotocol.DataType;
import org.corfudb.protocols.wireprotocol.ILogData;
import org.corfudb.protocols.wireprotocol.LogData;
import org.corfudb.runtime.exceptions.DataCorruptionException;
import org.corfudb.runtime.exceptions.OverwriteException;
import org.corfudb.util.serializer.Serializers;
import org.junit.Test;


/**
 * Created by maithem on 11/2/16.
 */
public class StreamLogFilesTest extends AbstractCorfuTest {

    private String getDirPath() {
        return PARAMETERS.TEST_TEMP_DIR;
    }

    private ServerContext getContext() {
        String path = getDirPath();
        return new ServerContextBuilder()
            .setLogPath(path)
            .setMemory(false)
            .build();
    }

    @Test
    public void testWriteReadWithChecksum() {
        // Enable checksum, then append and read the same entry
        StreamLog log = new StreamLogFiles(getContext(), false);
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        long address0 = 0;
        log.append(address0, new LogData(DataType.DATA, b));
        assertThat(log.read(address0).getPayload(null)).isEqualTo(streamEntry);

        // Disable checksum, then append and read then same entry
        // An overwrite exception should occur, since we are writing the
        // same entry.
        final StreamLog newLog = new StreamLogFiles(getContext(), true);
        assertThatThrownBy(() -> {
            newLog
                    .append(address0, new LogData(DataType.DATA, b));
        })
                .isInstanceOf(OverwriteException.class);
        assertThat(log.read(address0).getPayload(null)).isEqualTo(streamEntry);
    }

    @Test
    public void testBatchWrite() throws Exception {
        ServerContext sc = getContext();
        StreamLog log = new StreamLogFiles(sc, false);

        // A range write that spans two segments
        final int numSegments = 2;
        final int numIter = StreamLogFiles.RECORDS_PER_LOG_FILE * numSegments;
        List<LogData> writeEntries = new ArrayList<>();
        for (int x = 0; x < numIter; x++) {
            writeEntries.add(getEntry(x));
        }

        log.append(writeEntries);
        log.sync(true);

        StreamLog log2 = new StreamLogFiles(sc, false);
        List<LogData> readEntries = readRange(0, numIter, log2);
        assertThat(writeEntries).isEqualTo(readEntries);
    }

    @Test
    public void testRangeWriteTrim() throws Exception {
        StreamLog log = new StreamLogFiles(getContext(), false);

        // A range write that has the first 25% of its entries trimmed
        final int numEntries = 1000;
        final double trimmedRatio = .25;
        List<LogData> entries = new ArrayList<>();

        for (int x = 0; x < numEntries; x++) {
            if (x < trimmedRatio * numEntries) {
                entries.add(LogData.getTrimmed(x));
            } else {
                entries.add(getEntry(x));
            }
        }

        log.append(entries);
        long trimMarkAfterWrite = (long) (numEntries * trimmedRatio);
        assertThat(log.getTrimMark()).isEqualTo(trimMarkAfterWrite);
    }

    @Test
    public void testRangeWriteAfterPrefixTrim() throws Exception {
        // This test tries to write a range right after a part of the range to be
        // written is prefix trimmed.
        ServerContext sc = getContext();
        StreamLog log = new StreamLogFiles(sc, false);
        final long trimMark = 1000;
        final long trimOverlap = 100;
        final long numEntries = 200;
        log.prefixTrim(trimMark);

        List<LogData> entries = new ArrayList<>();
        for (long x = 0; x < numEntries; x++) {
            long address = trimMark - trimOverlap + x;
            entries.add(getEntry(address));
        }

        log.append(entries);

        StreamLog log2 = new StreamLogFiles(sc, false);
        List<LogData> readEntries = readRange(0L, trimMark - trimOverlap + numEntries, log2);

        List<LogData> notTrimmed = new ArrayList<>();
        for (LogData entry : readEntries) {
            if (!entry.isTrimmed()) {
                notTrimmed.add(entry);
            }
        }

        assertThat((long) notTrimmed.size()).isEqualTo(numEntries - trimOverlap - 1);
    }

    @Test
    public void badRangeWrite() throws Exception {
        ServerContext sc = getContext();
        StreamLog log = new StreamLogFiles(sc, false);

        List<LogData> largeRange = new ArrayList<>();
        List<LogData> nonSequentialRange = new ArrayList<>();
        final int numSegments = 3;
        final int skipGap = 10;
        final int numEntries = numSegments * StreamLogFiles.RECORDS_PER_LOG_FILE;
        // Generate two invalid ranges, a large range and a non-sequential range
        for (long address = 0; address < numEntries; address++) {
            largeRange.add(getEntry(address));
            if (address % skipGap == 0) {
                nonSequentialRange.add(getEntry(address));
            }
        }

        String logDir = sc.getServerConfig().get("--log-path") + File.separator + "log";
        File file = new File(logDir);
        long logSize = FileUtils.sizeOfDirectory(file);

        // Verify that range writes that span more than 2 segments are rejected
        assertThatThrownBy(() -> log.append(largeRange))
                .isInstanceOf(IllegalArgumentException.class);
        log.sync(true);

        // Verify that a non-sequential range write is rejected
        assertThatThrownBy(() -> log.append(nonSequentialRange))
                .isInstanceOf(IllegalArgumentException.class);
        log.sync(true);

        // Verify that no entries have been written to the log
        long logSize2 = FileUtils.sizeOfDirectory(file);
        assertThat(logSize2).isEqualTo(logSize);
    }

    @Test
    public void testRangeOverwrite() throws Exception {
        ServerContext sc = getContext();
        StreamLog log = new StreamLogFiles(sc, false);
        final int numIter = 500;

        List<LogData> entries = new ArrayList<>();
        for (long x = 0; x < numIter; x++) {
            entries.add(getEntry(x));
        }

        String logDir = sc.getServerConfig().get("--log-path") + File.separator + "log";
        File file = new File(logDir);
        // Write the same range twice
        log.append(entries);
        log.sync(true);
        long logSize = FileUtils.sizeOfDirectory(file);
        log.append(entries);
        log.sync(true);
        long logSize2 = FileUtils.sizeOfDirectory(file);
        assertThat(logSize2).isEqualTo(logSize);
    }

    @Test
    public void writingTrimmedEntries() throws Exception {
        ServerContext sc = getContext();
        StreamLog log = new StreamLogFiles(sc, false);

        final long trimMark = 400;
        final long numEntries = trimMark + 200;
        log.prefixTrim(trimMark);

        List<LogData> entries = new ArrayList<>();
        for (int x = 0; x < numEntries; x++) {
            entries.add(LogData.getTrimmed(x));
        }

        String logDir = sc.getServerConfig().get("--log-path") + File.separator + "log";
        File file = new File(logDir);
        long logSize = FileUtils.sizeOfDirectory(file);
        log.append(entries);
        log.sync(true);
        long logSize2 = FileUtils.sizeOfDirectory(file);
        assertThat(logSize2).isEqualTo(logSize);
    }

    /**
     * Reads the range [a, b) from a specific log
     */
    List<LogData> readRange(long a, long b, StreamLog log) {
        List<LogData> entries = new ArrayList<>();
        for (long x = a; x < b; x++) {
            entries.add(log.read(x));
        }
        return entries;
    }

    LogData getEntry(long x) {
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        LogData ld = new LogData(DataType.DATA, b);
        ld.setGlobalAddress((long) x);
        return ld;
    }

    @Test
    public void testOverwriteException() {
        StreamLog log = new StreamLogFiles(getContext(), false);
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        long address0 = 0;
        log.append(address0, new LogData(DataType.DATA, b));

        assertThatThrownBy(() -> log.append(address0, new LogData(DataType.DATA, b)))
                .isInstanceOf(OverwriteException.class);
    }

    @Test
    public void testReadingUnknownAddress() {
        StreamLog log = new StreamLogFiles(getContext(), false);
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);

        long address0 = 0;
        long address1 = 1;
        long address2 = 2;

        log.append(address0, new LogData(DataType.DATA, b));
        log.append(address2, new LogData(DataType.DATA, b));
        assertThat(log.read(address1)).isNull();
    }

    @Test
    public void testStreamLogBadChecksum() {
        // This test generates a stream log file without computing checksums, then
        // tries to read from the same log file with checksum enabled. The expected
        // behaviour is to throw a DataCorruptionException because a checksum cannot
        // be computed for stream entries that haven't been written with a checksum
        StreamLog log = new StreamLogFiles(getContext(), true);
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        long address0 = 0;
        log.append(address0, new LogData(DataType.DATA, b));

        assertThat(log.read(address0).getPayload(null)).isEqualTo(streamEntry);

        log.close();

        // Re-open stream log with checksum enabled
        assertThatThrownBy(() -> new StreamLogFiles(getContext(), false))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testStreamLogDataCorruption() throws Exception {
        // This test manipulates a log file directly and manipulates
        // log records by overwriting some parts of the record simulating
        // different data corruption scenarios
        String logDir = getContext().getServerConfig().get("--log-path") + File.separator + "log";
        StreamLog log = new StreamLogFiles(getContext(), false);
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        // Write to two segments
        long address0 = 0;
        long address1 = StreamLogFiles.RECORDS_PER_LOG_FILE + 1L;
        log.append(address0, new LogData(DataType.DATA, b));
        log.append(address1, new LogData(DataType.DATA, b));

        assertThat(log.read(address0).getPayload(null)).isEqualTo(streamEntry);
        log.close();

        final int OVERWRITE_DELIMITER = 0xFFFF;
        final int OVERWRITE_BYTES = 4;

        // Overwrite 2 bytes of the checksum and 2 bytes of the entry's address
        String logFilePath1 = logDir + File.separator + 0 + ".log";
        String logFilePath2 = logDir + File.separator + 1 + ".log";
        RandomAccessFile file1 = new RandomAccessFile(logFilePath1, "rw");
        RandomAccessFile file2 = new RandomAccessFile(logFilePath2, "rw");
        ByteBuffer metaDataBuf = ByteBuffer.allocate(METADATA_SIZE);
        file1.getChannel().read(metaDataBuf);
        metaDataBuf.flip();

        Metadata metadata = Metadata.parseFrom(metaDataBuf.array());

        final int offset1 = METADATA_SIZE + metadata.getLength();
        final int offset2 = METADATA_SIZE + metadata.getLength() + Short.BYTES + OVERWRITE_BYTES;

        // Corrupt delimiter in the first segment

        file1.seek(offset1);
        file1.writeShort(0);
        file1.close();

        assertThatThrownBy(() -> new StreamLogFiles(getContext(), false).read(0L))
                .isInstanceOf(DataCorruptionException.class);

        // Corrupt metadata in the second segment
        file2.seek(offset2);
        file2.writeInt(OVERWRITE_DELIMITER);
        file2.close();

        assertThatThrownBy(() -> new StreamLogFiles(getContext(), false))
                .isInstanceOf(DataCorruptionException.class);
    }

    @Test
    public void multiThreadedReadWrite() throws Exception {
        String logDir = getDirPath();
        StreamLog log = new StreamLogFiles(getContext(), false);

        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);

        final int num_threads = PARAMETERS.CONCURRENCY_SOME;
        final int num_entries = PARAMETERS.NUM_ITERATIONS_LOW;

        scheduleConcurrently(num_threads, threadNumber -> {
            int base = threadNumber * num_entries;
            for (int i = base; i < base + num_entries; i++) {
                long address = (long) i;
                log.append(address, new LogData(DataType.DATA, b));
            }
        });

        executeScheduled(num_threads, PARAMETERS.TIMEOUT_LONG);

        // verify that addresses 0 to 2000 have been used up
        for (int x = 0; x < num_entries * num_threads; x++) {
            long address = (long) x;
            LogData data = log.read(address);
            byte[] bytes = (byte[]) data.getPayload(null);
            assertThat(bytes).isEqualTo(streamEntry);
        }
    }

    @Test
    @SuppressWarnings("checkstyle:magicnumber")
    public void testSync() throws Exception {
        StreamLogFiles log = new StreamLogFiles(getContext(), false);
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        long seg1 = StreamLogFiles.RECORDS_PER_LOG_FILE * 0 + 1;
        long seg2 = StreamLogFiles.RECORDS_PER_LOG_FILE * 1 + 1;
        long seg3 = StreamLogFiles.RECORDS_PER_LOG_FILE * 2 + 1;

        log.append(seg1, new LogData(DataType.DATA, b));
        log.append(seg2, new LogData(DataType.DATA, b));
        log.append(seg3, new LogData(DataType.DATA, b));

        assertThat(log.getChannelsToSync().size()).isEqualTo(3);

        log.sync(true);

        assertThat(log.getChannelsToSync().size()).isEqualTo(0);
    }

    @Test
    public void testSameAddressTrim() throws Exception {
        StreamLogFiles log = new StreamLogFiles(getContext(), false);

        // Trim an unwritten address
        long address = 0L;
        log.trim(address);

        // Verify that the unwritten address trim is not persisted
        SegmentHandle sh = log.getSegmentHandleForAddress(address);
        assertThat(sh.getPendingTrims().size()).isEqualTo(0);

        // Write to the same address
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);

        log.append(address, new LogData(DataType.DATA, b));

        // Verify that the address has been written
        assertThat(log.read(address)).isNotNull();

        // Trim the address
        log.trim(address);
        sh = log.getSegmentHandleForAddress(address);
        assertThat(sh.getPendingTrims().contains(address)).isTrue();

        // Write to a trimmed address
        assertThatThrownBy(() -> log.append(address, new LogData(DataType.DATA, b)))
                .isInstanceOf(OverwriteException.class);

        // Read trimmed address
        assertThat(log.read(address).isTrimmed()).isTrue();
    }

    private void writeToLog(StreamLog log, long address) {
        ByteBuf b = Unpooled.buffer();
        byte[] streamEntry = "Payload".getBytes();
        Serializers.CORFU.serialize(streamEntry, b);
        log.append(address, new LogData(DataType.DATA, b));
    }

    @Test
    public void testTrim() throws Exception {
        StreamLogFiles log = new StreamLogFiles(getContext(), false);
        final int logChunk = StreamLogFiles.RECORDS_PER_LOG_FILE / 2;

        // Write to the addresses then trim the addresses that span two log files
        for (long x = 0; x < logChunk; x++) {
            writeToLog(log, x);
        }

        // Verify that an incomplete log segment isn't compacted
        for (long x = 0; x < logChunk; x++) {
            log.trim(x);
        }

        log.compact();

        SegmentHandle sh = log.getSegmentHandleForAddress(logChunk);

        assertThat(logChunk).isGreaterThan(StreamLogFiles.TRIM_THRESHOLD);
        assertThat(sh.getPendingTrims().size()).isEqualTo(logChunk);
        assertThat(sh.getTrimmedAddresses().size()).isEqualTo(0);

        // Fill the rest of the log segment and compact
        for (long x = logChunk; x < logChunk * 2; x++) {
            writeToLog(log, x);
        }

        // Verify the pending trims are compacted after the log segment has filled
        File file = new File(sh.getFileName());
        long sizeBeforeCompact = file.length();
        log.compact();
        file = new File(sh.getFileName());
        long sizeAfterCompact = file.length();

        assertThat(sizeAfterCompact).isLessThan(sizeBeforeCompact);

        // Reload the segment handler and check that the first half of the segment has been trimmed
        sh = log.getSegmentHandleForAddress(logChunk);
        assertThat(sh.getTrimmedAddresses().size()).isEqualTo(logChunk);
        assertThat(sh.getKnownAddresses().size()).isEqualTo(logChunk);

        for (long x = logChunk; x < logChunk * 2; x++) {
            assertThat(sh.getKnownAddresses().get(x)).isNotNull();
        }

        // Verify that the trimmed addresses cannot be written to or read from after compaction
        for (long x = 0; x < logChunk; x++) {
            long address = x;
            assertThat(log.read(address).isTrimmed()).isTrue();
            assertThatThrownBy(() -> writeToLog(log, address))
                    .isInstanceOf(OverwriteException.class);
        }
    }

    @Test
    public void testWritingFileHeader() throws Exception {
        StreamLogFiles log = new StreamLogFiles(getContext(), false);
        writeToLog(log, 0L);
        log.sync(true);
        StreamLogFiles log2 = new StreamLogFiles(getContext(), false);
        writeToLog(log2, 1L);
        log2.sync(true);
        StreamLogFiles log3 = new StreamLogFiles(getContext(), false);
        assertThat(log3.read(1L)).isNotNull();
    }

    @Test
    public void testGetGlobalTail() {
        StreamLogFiles log = new StreamLogFiles(getContext(), false);

        assertThat(log.getGlobalTail()).isEqualTo(0);

        // Write to multiple segments
        final int segments = 3;
        long lastAddress = segments * StreamLogFiles.RECORDS_PER_LOG_FILE;
        for (long x = 0; x <= lastAddress; x++){
            writeToLog(log, x);
            assertThat(log.getGlobalTail()).isEqualTo(x);
        }

        // Restart and try to retrieve the global tail
        log = new StreamLogFiles(getContext(), false);
        assertThat(log.getGlobalTail()).isEqualTo(lastAddress);

        // Advance the tail some more
        final long tailDelta = 5;
        for (long x = lastAddress + 1; x <= lastAddress + tailDelta; x++){
            writeToLog(log, x);
            assertThat(log.getGlobalTail()).isEqualTo(x);
        }

        // Restart and try to retrieve the global tail one last time
        log = new StreamLogFiles(getContext(), false);
        assertThat(log.getGlobalTail()).isEqualTo(lastAddress + tailDelta);
    }

    @Test
    public void testPrefixTrim() {
        String logDir = getContext().getServerConfig().get("--log-path") + File.separator + "log";
        StreamLog log = new StreamLogFiles(getContext(), false);

        // Write 50 segments and trim the first 25
        final long numSegments = 50;
        final long filesPerSegment = 3;
        for(long x = 0; x < numSegments * StreamLogFiles.RECORDS_PER_LOG_FILE; x++) {
            writeToLog(log, x);
        }

        File logs = new File(logDir);

        assertThat((long) logs.list().length).isEqualTo(numSegments * filesPerSegment);

        final long endSegment = 25;
        long trimAddress = endSegment * StreamLogFiles.RECORDS_PER_LOG_FILE + 1;

        // Get references to the segments that will be trimmed
        Set<SegmentHandle> trimmedHandles = new HashSet();
        for (SegmentHandle sh : ((StreamLogFiles)log).getSegmentHandles()) {
            if (sh.getSegment() < endSegment) {
                trimmedHandles.add(sh);
            }
        }

        log.prefixTrim(trimAddress);
        log.compact();

        // Verify that the segments have been removed
        assertThat(((StreamLogFiles)log).getSegmentHandles().size()).isEqualTo((int) endSegment);

        // Verify that first 25 segments have been deleted
        String[] afterTrimFiles = logs.list();
        assertThat(afterTrimFiles).hasSize((int)((numSegments - endSegment + 1) * filesPerSegment));

        Set<String> fileNames = new HashSet(Arrays.asList(afterTrimFiles));
        for (long x = endSegment + 1; x < numSegments; x++) {
            String logFile = Long.toString(x) + ".log";
            String trimmedLogFile = StreamLogFiles.getTrimmedFilePath(logFile);
            String pendingLogFile = StreamLogFiles.getPendingTrimsFilePath(logFile);

            assertThat(fileNames).contains(logFile);
            assertThat(fileNames).contains(trimmedLogFile);
            assertThat(fileNames).contains(pendingLogFile);
        }

        // Try to trim an address that is less than the new starting address
        // This shouldn't throw an exception
        log.prefixTrim(trimAddress);

        long trimmedExceptions = 0;

        // Try to read trimmed addresses
        for(long x = 0; x < numSegments * StreamLogFiles.RECORDS_PER_LOG_FILE; x++) {
            ILogData logData = log.read(x);
            if(logData.isTrimmed()) {
                trimmedExceptions++;
            }
        }

        // Verify that the trimmed segment channels are closed
        for (SegmentHandle sh : trimmedHandles) {
            assertThat(sh.getWriteChannel().isOpen()).isFalse();
            assertThat(sh.getPendingTrimChannel().isOpen()).isFalse();
            assertThat(sh.getTrimmedChannel().isOpen()).isFalse();
        }

        // Address 0 is not reflected in trimAddress
        assertThat(trimmedExceptions).isEqualTo(trimAddress + 1);
    }

    @Test
    public void testPrefixTrimAndStartUp() {
        StreamLog log = new StreamLogFiles(getContext(), false);
        log.prefixTrim(StreamLogFiles.RECORDS_PER_LOG_FILE / 2);
        log.compact();
        log = new StreamLogFiles(getContext(), false);
        final long midSegmentAddress = RECORDS_PER_LOG_FILE + 5;
        log.prefixTrim(midSegmentAddress);
        log.compact();
        log = new StreamLogFiles(getContext(), false);

        assertThat(log.getGlobalTail()).isEqualTo(midSegmentAddress);
        assertThat(log.getTrimMark()).isEqualTo(midSegmentAddress + 1);
    }

    @Test
    public void testPrefixTrimAfterRestart() {
        String logDir = getContext().getServerConfig().get("--log-path") + File.separator + "log";
        StreamLog log = new StreamLogFiles(getContext(), false);

        final long numSegments = 3;
        for (long x  = 0; x < RECORDS_PER_LOG_FILE * numSegments; x++) {
            writeToLog(log, x);
        }

        final long trimMark = RECORDS_PER_LOG_FILE * 2 + 100;
        log.prefixTrim(trimMark);
        log.close();

        log = new StreamLogFiles(getContext(), false);
        log.compact();

        File logs = new File(logDir);
        final int lastTwoSegmentsFiles = 3 * 2;
        assertThat(logs.list()).hasSize(lastTwoSegmentsFiles);
    }

    /**
     * Generates and writes 3 files worth data. Then resets the stream log and verifies that the
     * files and data is cleared.
     */
    @Test
    public void testResetStreamLog() {
        String logDir = getContext().getServerConfig().get("--log-path") + File.separator + "log";
        StreamLog log = new StreamLogFiles(getContext(), false);

        final long numSegments = 3;
        for (long x = 0; x < RECORDS_PER_LOG_FILE * numSegments; x++) {
            writeToLog(log, x);
        }
        final long filesToBeTrimmed = 1;
        log.prefixTrim(RECORDS_PER_LOG_FILE * (filesToBeTrimmed + 1));
        log.compact();

        File logsDir = new File(logDir);

        final int expectedFilesBeforeReset = (int) ((numSegments - filesToBeTrimmed) * 3);
        final long globalTailBeforeReset = (RECORDS_PER_LOG_FILE * numSegments) - 1;
        final long trimMarkBeforeReset = (RECORDS_PER_LOG_FILE * (filesToBeTrimmed + 1)) + 1;
        assertThat(logsDir.list()).hasSize(expectedFilesBeforeReset);
        assertThat(log.getGlobalTail()).isEqualTo(globalTailBeforeReset);
        assertThat(log.getTrimMark()).isEqualTo(trimMarkBeforeReset);

        log.reset();

        // Files have been deleted and new 0.log files are created due to reset.
        Arrays.stream(logsDir.list()).forEach(
                s -> assertThat(new File(s).length()).isEqualTo(0L));

        final int expectedFilesAfterReset = 3;
        final long globalTailAfterReset = 0L;
        final long trimMarkAfterReset = 0L;
        assertThat(logsDir.list()).hasSize(expectedFilesAfterReset);
        assertThat(log.getGlobalTail()).isEqualTo(globalTailAfterReset);
        assertThat(log.getTrimMark()).isEqualTo(trimMarkAfterReset);
    }
}