package team.creative.creativecore.common.gui.integration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.common.gui.GuiLayer;
import team.creative.creativecore.common.gui.sync.LayerClosePacket;
import team.creative.creativecore.common.gui.sync.LayerOpenPacket;
import team.creative.creativecore.common.network.CreativePacket;

public class ContainerIntegration extends Container implements IGuiIntegratedParent {
    
    private List<GuiLayer> layers = new ArrayList<>();
    private final PlayerEntity player;
    
    public ContainerIntegration(ContainerType<ContainerIntegration> type, int id, PlayerEntity player, GuiLayer layer) {
        super(type, id);
        this.player = player;
        layer.setParent(this);
        this.layers.add(layer);
        layer.init();
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        tick();
    }
    
    public void tick() {
        for (GuiLayer layer : layers)
            layer.tick();
    }
    
    @Override
    public List<GuiLayer> getLayers() {
        return layers;
    }
    
    @Override
    public GuiLayer getTopLayer() {
        return layers.get(layers.size() - 1);
    }
    
    @Override
    public boolean isContainer() {
        return true;
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        for (GuiLayer layer : layers)
            layer.closed();
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
    
    @Override
    public PlayerEntity getPlayer() {
        return player;
    }
    
    @Override
    public boolean isClient() {
        return player.world.isRemote;
    }
    
    @Override
    public void openLayer(GuiLayer layer) {
        layers.add(layer);
        layer.init();
    }
    
    @Override
    public GuiLayer openLayer(LayerOpenPacket packet) {
        packet.execute(player);
        if (isClient())
            CreativeCore.NETWORK.sendToServer(packet);
        else
            CreativeCore.NETWORK.sendToClient(packet, (ServerPlayerEntity) player);
        
        return layers.get(layers.size() - 1);
    }
    
    @Override
    public void closeTopLayer() {
        int index = layers.size() - 1;
        sendPacket(new LayerClosePacket());
        layers.remove(index);
        if (layers.isEmpty())
            if (isClient())
                Minecraft.getInstance().displayGuiScreen((Screen) null);
            else
                ((ServerPlayerEntity) player).closeContainer();
    }
    
    @Override
    public void closeLayer(int layer) {
        layers.remove(layer);
        if (layers.isEmpty())
            if (isClient())
                Minecraft.getInstance().displayGuiScreen((Screen) null);
            else
                ((ServerPlayerEntity) player).closeContainer();
    }
    
    public void sendPacket(CreativePacket packet) {
        if (isClient())
            sendPacketToServer(packet);
        else
            sendPacketToClient(packet);
    }
    
    public void sendPacketToServer(CreativePacket packet) {
        CreativeCore.NETWORK.sendToServer(packet);
    }
    
    public void sendPacketToClient(CreativePacket packet) {
        CreativeCore.NETWORK.sendToClient(packet, (ServerPlayerEntity) player);
    }
    
}
