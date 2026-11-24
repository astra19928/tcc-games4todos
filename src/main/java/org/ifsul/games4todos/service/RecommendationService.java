package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.RecommendedMod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.ModDisabilityRepository;
import org.ifsul.games4todos.repository.ModRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private static final int LIMIT = 8;
    private final ModRepository modRepository;
    private final ModDisabilityRepository modDisabilityRepository;

    public List<RecommendedMod> recommend(User user) {
        if (user == null)
            return plain(modRepository.findTop8ByOrderByReleaseDateDescIdDesc());

        List<Integer> typeIds = modRepository.findDisabilityTypeIdsByUserId(user.getId());
        if (typeIds.isEmpty())
            return plain(modRepository.findTop8ByOrderByReleaseDateDescIdDesc());

        List<Mod> mods = modRepository.findBestMatches(typeIds, LIMIT);
        if (mods.isEmpty()) return List.of();

        List<Integer> modIds = mods.stream().map(Mod::getId).toList();

        // modId -> names of the user's necessities this mod covers
        Map<Integer, List<String>> matches = modDisabilityRepository
                .findMatches(modIds, typeIds).stream()
                .collect(Collectors.groupingBy(
                        md -> md.getMod().getId(),
                        Collectors.mapping(md -> md.getType().getName(), Collectors.toList())));

        return mods.stream()
                .map(m -> new RecommendedMod(m.getId(), m.getFullTitle(), m.getHref(), m.getModIconSrc(),
                        matches.getOrDefault(m.getId(), List.of())))
                .toList();
    }

    private List<RecommendedMod> plain(List<Mod> mods) {
        return mods.stream()
                .map(m -> new RecommendedMod(m.getId(), m.getFullTitle(), m.getHref(), m.getModIconSrc(), List.of()))
                .toList();
    }
}