package com.cb.ffmpeg.model;

/**
 * Created by Administrator on 2018/4/18.
 */

public class LocalMediaCutConfig {
    /**
     * 开始时间
     */
    private long start;
    /**
     * 截止时间
     */
    private long end;
    /**
     * 输入路径
     */
    private String inputPath;
    /**
     * 输出路径
     */
    private String outputPath;

    private LocalMediaCutConfig(Buidler buidler) {
        this.start = buidler.start;
        this.end = buidler.end;
        this.inputPath = buidler.inputPath;
        this.outputPath = buidler.outputPath;
    }

    public static class Buidler {
        /**
         * 开始时间
         */
        private long start;
        /**
         * 截止时间
         */
        private long end;
        /**
         * 输入路径
         */
        private String inputPath;
        /**
         * 输出路径
         */
        private String outputPath;

        public LocalMediaCutConfig build() {
            return new LocalMediaCutConfig(this);
        }

        public Buidler setStart(long start) {
            this.start = start;
            return this;
        }

        public Buidler setEnd(long end) {
            this.end = end;
            return this;
        }

        public Buidler setInputPath(String inputPath) {
            this.inputPath = inputPath;
            return this;
        }

        /**
         * 可不设置
         * @param outputPath
         * @return
         */
        public Buidler setOutputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

    }


    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
