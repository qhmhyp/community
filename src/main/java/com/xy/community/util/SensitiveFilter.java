package com.xy.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            LOGGER.error("加载敏感词文件失败： " + e.getMessage());
        }
    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {

            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            tempNode = subNode;

            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     */

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while(begin < text.length()){
            if(position < text.length()) {
                Character c = text.charAt(position);

                // 跳过符号
                if (isSymbol(c)) {
                    if (tempNode == rootNode) {
                        begin++;
                        sb.append(c);
                    }
                    position++;
                    continue;
                }

                // 检查下级节点
                tempNode = tempNode.getSubNode(c);
                if (tempNode == null) {
                    // 以begin开头的字符串不是敏感词
                    sb.append(text.charAt(begin));
                    // 进入下一个位置
                    position = ++begin;
                    // 重新指向根节点
                    tempNode = rootNode;
                }
                // 发现敏感词
                else if (tempNode.isKeywordEnd()) {
                    sb.append(REPLACEMENT);
                    begin = ++position;
                }
                // 检查下一个字符
                else {
                    position++;
                }
            }
            // position遍历越界仍未匹配到敏感词
            else{
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }
        }
        return sb.toString();
    }

    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode {

        private boolean isKeywordEnd = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        private boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }

}
