# Prompt: Write Java e2e test for a new ARM Spector scenario

You are running inside the GitHub Actions workflow
`.github/workflows/generate-java-e2e-for-arm-spector.yml`. Your job is to
**add or update Java JUnit tests** for the ARM Spector scenarios listed below,
following the existing instructions and conventions.

## Authoritative instructions

Read and follow `.github/instructions/http-client-java.instructions.md`,
specifically the **"Add end-to-end (e2e) test case"** section. The mechanical
steps in that section (Setup.ps1, npm install, regen, spector-start/stop, mvn
commands) have **already been performed** by the workflow before you start.

Your scope is limited to:

1. Reading the affected `main.tsp` (and `client.tsp` if it exists next to it).
2. Reading the generated Java client / model classes for the scenario under
   `packages/http-client-java/generator/http-client-generator-test/src/main/java/<package>/`.
3. Adding a new test class (or updating an existing one) at
   `packages/http-client-java/generator/http-client-generator-test/src/test/java/<package>/<Scenario>Tests.java`.
   The test class **must not be placed under any `generated/` folder** (those are
   git-ignored for ARM specs).
4. Making sure the test class compiles. Do **not** run `mvn test` yourself —
   the workflow runs it after you finish.

## Scenarios in scope for this run

The following ARM scenario folders were added or modified in the typespec-azure PR
that triggered this workflow:

<!-- SCENARIO_LIST -->
{{SCENARIO_LIST}}
<!-- /SCENARIO_LIST -->

The corresponding `main.tsp` files live at those paths inside the typespec-azure
checkout at `../typespec-azure/` (sibling to this repo on the runner).

## Hard constraints

- Do **not** modify any file outside `packages/http-client-java/generator/http-client-generator-test/src/test/java/**`.
- Do **not** edit `package.json`, lockfiles, `pom.xml`, generated sources, or
  workflow files.
- Do **not** commit, push, or open PRs. The workflow handles git operations.
- Follow the existing test class style: one test class per scenario file,
  named `<Scenario>Tests.java`, not extending any base class, using JUnit 5.
- Use the generated `Client` / `Builder` types and the `models` package for
  the scenario; mimic patterns from neighboring non-generated test classes
  (e.g. siblings under `src/test/java/`).
- **Import discipline (critical):** before writing any `import` for a model,
  open the generated source under `src/main/java/<scenario-package>/models/`
  and confirm the type actually lives there. Shared ARM types
  (e.g. `ManagedServiceIdentity`, `UserAssignedIdentity`, `SystemData`) are
  often re-exported under a `commontypes` package — use the exact package
  declared by the generated file, never guess. Compile errors of the form
  "incompatible types: X cannot be converted to Y" almost always mean an
  import was taken from the wrong package.

When done, print a short summary listing the test files you added or updated.
