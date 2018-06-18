/*
 *
 * SuperbVote-Expansion
 * Copyright (C) 2018 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package com.extendedclip.papi.expansion.superbvote;

import io.minimum.minecraft.superbvote.SuperbVote;
import io.minimum.minecraft.superbvote.util.PlayerVotes;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SuperbVoteExpansion extends PlaceholderExpansion {

	private SuperbVote pl;
	
	private final String VERSION = getClass().getPackage().getImplementationVersion();
	
	@Override
	public boolean register() {
		pl = (SuperbVote) Bukkit.getPluginManager().getPlugin("SuperbVote");
		if (pl == null) {
			return false;
		}
		return super.register();
	}
	
	@Override
	public String getAuthor() {
		return "clip";
	}

	@Override
	public String getIdentifier() {
		return "superbvote";
	}

	@Override
	public String getPlugin() {
		return "SuperbVote";
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public String onPlaceholderRequest(Player p, String identifier) {

		if (p == null) {
			return "";
		}
		
		switch (identifier) {
		case "votes":
			return String.valueOf(pl.getVoteStorage().getVotes(p.getUniqueId()).getVotes());
		case "has_voted":
			return pl.getVoteStorage().hasVotedToday(p.getUniqueId()) ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
		}
		
		if (identifier.startsWith("top_voter_name_")) {
			identifier = identifier.replace("top_voter_name_", "");
			try {
				int index = Integer.parseInt(identifier);
				List<PlayerVotes> top = pl.getVoteStorage().getTopVoters(index, 1);

				if (top.isEmpty()) {
					return "";
				}
				
				OfflinePlayer pl = Bukkit.getOfflinePlayer(top.get(0).getUuid());
				
				if (pl == null) {
					return "";
				}
				
				return pl.getName();
			} catch (NumberFormatException ex) {
				return "invalid index number";
			}
		}
		
		if (identifier.startsWith("top_voter_votes_")) {
			identifier = identifier.replace("top_voter_votes_", "");
			try {
				int index = Integer.parseInt(identifier);
				List<PlayerVotes> top = pl.getVoteStorage().getTopVoters(index, 1);
				if (top.isEmpty()) {
					return "";
				}
				return String.valueOf(top.get(0).getVotes());
			} catch (NumberFormatException ex) {
				return "invalid index number";
			}
		}
		
		return null;
	}

}
