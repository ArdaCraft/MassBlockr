package me.dags.blockr.app;

import me.dags.blockr.BlockInfo;

import javax.swing.*;
import java.awt.*;

public class MappingPopup extends JPanel {

    private final Frame parent;
    private final Mapping mapping;
    private final JComboBox<String> fromName;
    private final JCheckBox doMatchBiome;
    private final JSpinner biome;
    private final JSpinner fromMin;
    private final JSpinner fromMax;
    private final JComboBox<String> toName;
    private final JSpinner toMin;
    private final JSpinner toMax;

    public MappingPopup(Frame parent, Mapping mapping) {
        this.parent = parent;
        this.mapping = mapping;
        fromName = new JComboBox<>(mapping.fromBlocks);
        fromName.setSelectedItem(mapping.blockInfo.name);

        SpinnerModel biomeModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.biome, 0), 0, 100, 1);
        biome = new JSpinner(biomeModel);
        biome.setEnabled(mapping.blockInfo.biome >= 0);

        doMatchBiome = new JCheckBox(" Match BiomeID:");
        doMatchBiome.addActionListener(e -> biome.setEnabled(doMatchBiome.isSelected()));
        doMatchBiome.setSelected(biome.isEnabled());

        SpinnerModel minModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.min, 0), 0, 16, 1);
        fromMin = new JSpinner(minModel);
        SpinnerModel maxModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.max, 0), 0, 16, 1);
        fromMax = new JSpinner(maxModel);
        linkSpinners(fromMin, fromMax);

        toName = new JComboBox<>(mapping.toBlocks);
        toName.setSelectedItem(mapping.blockInfo.to.name);

        SpinnerModel toMinModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.to.min, 0), 0, 16, 1);
        toMin = new JSpinner(toMinModel);
        SpinnerModel toMaxModel = new SpinnerNumberModel(Math.max(mapping.blockInfo.to.max, 0), 0, 16, 1);
        toMax = new JSpinner(toMaxModel);
        linkSpinners(toMin, toMax);

        JButton done = new JButton("Done");
        done.addActionListener(e -> updateMapping());

        this.setLayout(new GridLayout(4, 1));
        this.add(row(fromName, doMatchBiome, biome, new JLabel(" Match Meta(s):"), fromMin, new JLabel(" to "), fromMax));
        this.add(row(new JLabel("Convert To:")));
        this.add(row(toName, new JLabel("  Meta:"), toMin, new JLabel(" to "), toMax));
        this.add(row(done));
    }

    private void linkSpinners(JSpinner min, JSpinner max) {
        min.addChangeListener(e -> {
            if ((int) min.getValue() - (int) max.getValue() > 0) {
                max.setValue(min.getValue());
            }
        });
        max.addChangeListener(e -> {
            if ((int) min.getValue() - (int) max.getValue() > 0) {
                min.setValue(max.getValue());
            }
        });
    }

    private JPanel row(Component... child) {
        JPanel row = new JPanel();
        for (Component panel : child) {
            row.add(panel);
        }
        return row;
    }

    public void updateMapping() {
        String nameTo = toName.getSelectedItem().toString();
        int toMinData = (int) toMin.getValue();
        int toMaxData = (int) toMax.getValue();
        BlockInfo to = new BlockInfo(nameTo, -1, toMinData, toMaxData, BlockInfo.EMPTY);

        String nameFrom = this.fromName.getSelectedItem().toString();
        int biomeId = doMatchBiome.isSelected() ? (int) biome.getValue() : -1;
        int fromMinData = (int) fromMin.getValue();
        int fromMaxData = (int) fromMax.getValue();
        BlockInfo from = new BlockInfo(nameFrom, biomeId, fromMinData, fromMaxData, to);

        mapping.blockInfo = from;
        mapping.update();
        parent.dispose();
    }
}
