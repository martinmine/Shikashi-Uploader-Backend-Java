package me.shikashi.img.resources;

import me.shikashi.img.S3ArchitecturalUpgrade;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by marti_000 on 28.07.2015.
 */
public class UpgradeResource extends ServerResource {
    @Get
    public void doStudd() {
        new S3ArchitecturalUpgrade().upgrade();
    }
}
