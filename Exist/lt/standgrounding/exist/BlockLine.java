package lt.standgrounding.exist;

import java.util.Collection;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_11_R1.AxisAlignedBB;

public class BlockLine {
	BlockLine(){
	}
	public boolean returnEntities(Collection<? extends Block> T, LivingEntity E){
		for(Block Q : T){
			if(IntersectsWith(Q, (Entity) E)){
				return true;
			}
		}
		return false;
	}
    public boolean IntersectsWith(Block b, Entity entity) {
        
        AxisAlignedBB bb = ((CraftEntity) entity).getHandle().getBoundingBox();
        
        double x = b.getLocation().getX();
        double y = b.getLocation().getY();
        double z = b.getLocation().getZ();
       
        return
                x + 1 >= bb.a &&
                x - 1 <= bb.d &&
                y + 1 >= bb.b &&
                y - 1 <= bb.e &&
                z + 1 >= bb.c &&
                z - 1 <= bb.f;
       
    }

}
