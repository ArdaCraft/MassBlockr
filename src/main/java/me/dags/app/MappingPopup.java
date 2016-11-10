package me.dags.app;

import me.dags.worldfixer.BlockInfo;

import javax.swing.*;
import java.awt.*;

public class MappingPopup extends JPanel {

    private final Frame parent;
    private final Mapping mapping;
    private final JComboBox<String> from;
    private final JCheckBox matchBiome;
    private final JSpinner biome;
    private final JSpinner min;
    private final JSpinner max;
    private final JComboBox<String> to;
    private final JSpinner data;

    public MappingPopup(Frame parent, Mapping mapping) {
        this.parent = parent;
        this.mapping = mapping;
        from = new JComboBox<>(mapping.blocks);
        from.setSelectedItem(mapping.blockInfo.name);

        SpinnerModel biomeModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.biome, 0), 0, 100, 1);
        biome = new JSpinner(biomeModel);
        biome.setEnabled(mapping.blockInfo.biome >= 0);

        matchBiome = new JCheckBox(" Match BiomeID:");
        matchBiome.addActionListener(e -> biome.setEnabled(matchBiome.isSelected()));
        matchBiome.setSelected(biome.isEnabled());

        SpinnerModel minModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.min, 0), 0, 16, 1);
        min = new JSpinner(minModel);

        SpinnerModel maxModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.max, 0), 0, 16, 1);
        max = new JSpinner(maxModel);

        to = new JComboBox<>(mapping.blocks);
        to.setSelectedItem(mapping.blockInfo.to.name);

        SpinnerModel dataModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.to.min, 0), 0, 16, 1);
        data = new JSpinner(dataModel);

        JButton done = new JButton("Done");
        done.addActionListener(e -> updateMapping());

        this.add(from);
        this.add(matchBiome);
        this.add(biome);
        this.add(new JLabel(" Match Meta(s):"));
        this.add(min);
        this.add(new JLabel(" to "));
        this.add(max);
        this.add(new JLabel(" Convert To ==>"));
        this.add(to);
        this.add(new JLabel("  Meta:"));
        this.add(data);
        this.add(done);
    }

    public void updateMapping() {
        String fromName = from.getSelectedItem().toString();
        int biomeId = matchBiome.isSelected() ? (int) biome.getValue() : -1;
        int minData = (int) min.getValue();
        int maxData = (int) max.getValue();
        String toName = to.getSelectedItem().toString();
        int toData = (int) data.getValue();
        BlockInfo to = new BlockInfo(toName, -1, toData, toData, BlockInfo.EMPTY);
        mapping.blockInfo = new BlockInfo(fromName, biomeId, minData, maxData, to);
        mapping.update();
        parent.dispose();
    }
}
